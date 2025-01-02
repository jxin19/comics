package com.homework.comics.workPricingPolicy

import com.homework.comics.work.application.WorkQueryService
import com.homework.comics.work.domain.Work
import com.homework.comics.work.domain.property.WorkTitle
import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyCommandService
import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyQueryService
import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceRequest
import com.homework.comics.workPricingPolicy.application.impl.WorkPricingPolicyCommandServiceImpl
import com.homework.comics.workPricingPolicy.repository.WorkPricingPolicyRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.Instant

@SpringBootTest
class WorkPricingPolicyCommandServiceTest {
    private lateinit var workPricingPolicyRepository: WorkPricingPolicyRepository
    private lateinit var workPricingPolicyQueryService: WorkPricingPolicyQueryService
    private lateinit var workQueryService: WorkQueryService
    private lateinit var workPricingPolicyCommandService: WorkPricingPolicyCommandService

    @BeforeEach
    fun setup() {
        workPricingPolicyRepository = mock(WorkPricingPolicyRepository::class.java)
        workPricingPolicyQueryService = mock(WorkPricingPolicyQueryService::class.java)
        workQueryService = mock(WorkQueryService::class.java)
        workPricingPolicyCommandService = WorkPricingPolicyCommandServiceImpl(
            workPricingPolicyRepository,
            workPricingPolicyQueryService,
            workQueryService
        )
    }

    private fun createRequest(
        workId: Long = 1L,
        isFree: Boolean = false,
        price: BigDecimal = BigDecimal("1000.00"),
        startAt: Instant = Instant.now(),
        endAt: Instant? = Instant.now().plusSeconds(3600)
    ) = WorkPricingPolicyServiceRequest(workId, isFree, price, startAt, endAt)

    @Test
    fun `가격정책 생성 성공`() {
        // given
        val request = createRequest()
        val work = Work(id = 1L, title = WorkTitle("작품"))
        val policy = request.toEntity(work)

        `when`(workQueryService.fetchById(request.workId)).thenReturn(work)
        `when`(workPricingPolicyRepository.save(any())).thenReturn(policy)

        // when
        val result = workPricingPolicyCommandService.create(request)

        // then
        assertEquals(request.workId, result.workId)
        assertEquals(request.price, result.price)
        verify(workPricingPolicyRepository).save(any())
    }

    @Test
    fun `가격정책 생성 실패 - 종료일이 시작일보다 이전`() {
        // given
        val request = createRequest(
            startAt = Instant.now(),
            endAt = Instant.now().minusSeconds(3600)
        )
        val work = Work(id = 1L, title = WorkTitle("작품"))

        `when`(workQueryService.fetchById(request.workId)).thenReturn(work)

        // when & then
        assertThrows<IllegalArgumentException>("종료일은 시작일보다 이후여야 합니다.") {
            workPricingPolicyCommandService.create(request)
        }
    }

    @Test
    fun `가격정책 수정 성공`() {
        // given
        val id = 1L
        val request = createRequest(price = BigDecimal("2000.00"))
        val work = Work(id = 1L, title = WorkTitle("작품"))
        val existingPolicy = request.toEntity(work)

        `when`(workPricingPolicyQueryService.fetchById(id)).thenReturn(existingPolicy)

        // when
        val result = workPricingPolicyCommandService.update(id, request)

        // then
        assertEquals(request.price, result.price)
        verify(workPricingPolicyQueryService).fetchById(id)
    }

}
