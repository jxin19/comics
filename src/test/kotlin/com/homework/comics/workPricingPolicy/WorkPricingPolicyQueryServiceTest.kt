package com.homework.comics.workPricingPolicy

import com.homework.comics.common.domain.Price
import com.homework.comics.work.domain.Work
import com.homework.comics.work.domain.property.WorkTitle
import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyQueryService
import com.homework.comics.workPricingPolicy.application.impl.WorkPricingPolicyQueryServiceImpl
import com.homework.comics.workPricingPolicy.domain.WorkPricingPolicy
import com.homework.comics.workPricingPolicy.repository.WorkPricingPolicyRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.Instant
import java.util.Optional

@SpringBootTest
class WorkPricingPolicyQueryServiceTest {
    private lateinit var workPricingPolicyRepository: WorkPricingPolicyRepository
    private lateinit var workPricingPolicyQueryService: WorkPricingPolicyQueryService

    @BeforeEach
    fun setup() {
        workPricingPolicyRepository = mock(WorkPricingPolicyRepository::class.java)
        workPricingPolicyQueryService = WorkPricingPolicyQueryServiceImpl(workPricingPolicyRepository)
    }

    @Test
    fun `가격정책 조회 성공`() {
        // given
        val id = 1L
        val work = Work(id = 1L, title = WorkTitle("작품"))
        val policy = WorkPricingPolicy(
            id = id,
            work = work,
            _isFree = false,
            price = Price(BigDecimal("1000")),
            startDate = Instant.now()
        )

        `when`(workPricingPolicyRepository.findById(id)).thenReturn(Optional.of(policy))

        // when
        val result = workPricingPolicyQueryService.findById(id)

        // then
        assertEquals(policy.id, result.id)
        assertEquals(policy.priceValue, result.price)
    }

    @Test
    fun `현재 가격정책 조회 성공`() {
        // given
        val workId = 1L
        val work = Work(id = workId, title = WorkTitle("작품"))
        val policy = WorkPricingPolicy(
            work = work,
            _isFree = false,
            price = Price(BigDecimal("1000")),
            startDate = Instant.now()
        )

        `when`(workPricingPolicyRepository.findCurrentPolicy(workId)).thenReturn(Optional.of(policy))

        // when
        val result = workPricingPolicyQueryService.findCurrentPolicyByWorkId(workId)

        // then
        assertEquals(workId, result.workId)
        assertEquals(policy.priceValue, result.price)
    }

    @Test
    fun `가격정책 조회 실패 - 존재하지 않는 정책`() {
        // given
        val id = 999L
        `when`(workPricingPolicyRepository.findById(id)).thenReturn(Optional.empty())

        // when & then
        assertThrows<NoSuchElementException>("존재하지 않는 가격정책입니다.") {
            workPricingPolicyQueryService.findById(id)
        }
    }
}
