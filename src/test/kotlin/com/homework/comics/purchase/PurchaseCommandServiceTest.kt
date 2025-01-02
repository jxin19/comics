package com.homework.comics.purchase

import com.homework.comics.common.component.AuthenticationFacade
import com.homework.comics.common.domain.Price
import com.homework.comics.common.exception.AdultOnlyAccessDeniedException
import com.homework.comics.member.domain.Member
import com.homework.comics.purchase.application.PurchaseCommandService
import com.homework.comics.purchase.application.impl.PurchaseCommandServiceImpl
import com.homework.comics.purchase.domain.Purchase
import com.homework.comics.purchase.repository.PurchaseRepository
import com.homework.comics.work.application.dto.WorkServiceResponse
import com.homework.comics.work.domain.Work
import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyQueryService
import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
@SpringBootTest
class PurchaseCommandServiceTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var purchaseRepository: PurchaseRepository
    private lateinit var workPricingPolicyQueryService: WorkPricingPolicyQueryService
    private lateinit var purchaseCommandService: PurchaseCommandService
    private lateinit var authenticationFacade: AuthenticationFacade

    private val testMemberId = 1L
    private val testWorkId = 1L

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        purchaseRepository = mock(PurchaseRepository::class.java)
        workPricingPolicyQueryService = mock(WorkPricingPolicyQueryService::class.java)
        authenticationFacade = mock(AuthenticationFacade::class.java)
        purchaseCommandService = PurchaseCommandServiceImpl(
            purchaseRepository,
            workPricingPolicyQueryService,
            authenticationFacade
        )

        // JWT 기본 설정
        `when`(authenticationFacade.getMemberId()).thenReturn(testMemberId)
        `when`(authenticationFacade.isAdultVerified()).thenReturn(false)
    }

    @Test
    fun `구매 성공`() = runTest {
        // given
        val workServiceResponse = WorkServiceResponse(
            id = testWorkId,
            title = "작품",
            description = "설명",
            author = "작가",
            adultOnly = false
        )
        val policyResponse = WorkPricingPolicyServiceResponse(
            id = 1L,
            workId = testWorkId,
            workServiceResponse = workServiceResponse,
            free = false,
            price = BigDecimal("1000"),
            startAt = Instant.now(),
            endAt = null
        )
        val purchase = Purchase(
            member = Member(id = testMemberId),
            work = Work(id = testWorkId),
            paidPrice = Price(BigDecimal("1000"))
        )

        `when`(workPricingPolicyQueryService.findCurrentPolicyByWorkId(testWorkId))
            .thenReturn(policyResponse)
        `when`(purchaseRepository.save(any())).thenReturn(purchase)

        // when
        val result = purchaseCommandService.purchase(testWorkId)

        // then
        assertEquals(testMemberId, result.memberId)
        assertEquals(testWorkId, result.workId)
        assertEquals(BigDecimal("1000.00"), result.paidPrice)
    }

    @Test
    fun `무료 작품 구매 실패`() = runTest {
        // given
        val workServiceResponse = WorkServiceResponse(
            id = testWorkId,
            title = "작품",
            description = "설명",
            author = "작가",
            adultOnly = false
        )
        val policyResponse = WorkPricingPolicyServiceResponse(
            id = 1L,
            workId = testWorkId,
            workServiceResponse = workServiceResponse,
            free = true,
            price = BigDecimal.ZERO,
            startAt = Instant.now(),
            endAt = null
        )

        `when`(workPricingPolicyQueryService.findCurrentPolicyByWorkId(testWorkId))
            .thenReturn(policyResponse)

        // when & then
        assertThrows<IllegalArgumentException> {
            runBlocking { purchaseCommandService.purchase(testWorkId) }
        }
    }

    @Test
    fun `성인인증 안된 사용자의 성인작품 구매 실패`() = runTest {
        // given
        val workServiceResponse = WorkServiceResponse(
            id = testWorkId,
            title = "작품",
            description = "설명",
            author = "작가",
            adultOnly = true  // 성인전용 작품
        )
        val policyResponse = WorkPricingPolicyServiceResponse(
            id = 1L,
            workId = testWorkId,
            workServiceResponse = workServiceResponse,
            free = false,
            price = BigDecimal("1000"),
            startAt = Instant.now(),
            endAt = null
        )

        `when`(workPricingPolicyQueryService.findCurrentPolicyByWorkId(testWorkId))
            .thenReturn(policyResponse)
        `when`(authenticationFacade.isAdultVerified()).thenReturn(false) // 성인인증 안됨

        // when & then
        assertThrows<AdultOnlyAccessDeniedException> {
            runBlocking { purchaseCommandService.purchase(testWorkId) }
        }
    }
}
