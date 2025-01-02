package com.homework.comics.workPricingPolicy

import com.homework.comics.common.domain.Price
import com.homework.comics.work.domain.Work
import com.homework.comics.work.domain.property.Author
import com.homework.comics.work.domain.property.Description
import com.homework.comics.work.domain.property.WorkTitle
import com.homework.comics.workPricingPolicy.domain.WorkPricingPolicy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.Instant

@SpringBootTest
class WorkPricingPolicyTest {
    private fun createValidWork() = Work(
        title = WorkTitle("작품제목"),
        description = Description("작품설명"),
        author = Author("작가이름")
    )

    private fun createValidPolicy(
        id: Long = 1L,
        work: Work = createValidWork(),
        isFree: Boolean = false,
        price: BigDecimal = BigDecimal("1000"),
        startDate: Instant = Instant.now(),
        endDate: Instant? = Instant.now().plusSeconds(3600)
    ) = WorkPricingPolicy(
        id = id,
        work = work,
        _isFree = isFree,
        price = Price(price),
        startDate = startDate,
        endDate = endDate
    )

    @Test
    fun `가격정책 생성 성공`() {
        val policy = createValidPolicy()

        assertEquals(1L, policy.id)
        assertFalse(policy.isFree)
        assertEquals(BigDecimal("1000.00"), policy.priceValue)
        assertTrue(policy.endAt!!.isAfter(policy.startAt))
    }

    @Test
    fun `가격정책 수정 성공`() {
        val policy = createValidPolicy()
        val newPolicy = createValidPolicy(
            isFree = true,
            price = BigDecimal("2000"),
            startDate = Instant.now().plusSeconds(3600),
            endDate = Instant.now().plusSeconds(7200)
        )

        policy.update(newPolicy)

        assertTrue(policy.isFree)
        assertEquals(BigDecimal("2000.00"), policy.priceValue)
        assertEquals(newPolicy.startAt, policy.startAt)
        assertEquals(newPolicy.endAt, policy.endAt)
    }

    @Test
    fun `가격정책 생성 실패 - 종료일이 시작일보다 이전`() {
        val startDate = Instant.now()
        val endDate = startDate.minusSeconds(3600)

        assertThrows<IllegalArgumentException>("종료일은 시작일보다 이후여야 합니다.") {
            createValidPolicy(startDate = startDate, endDate = endDate)
        }
    }

    @Test
    fun `가격정책 생성 실패 - 음수 가격`() {
        assertThrows<IllegalArgumentException>("가격은 0 이상이어야 합니다.") {
            createValidPolicy(price = BigDecimal("-1000"))
        }
    }
}
