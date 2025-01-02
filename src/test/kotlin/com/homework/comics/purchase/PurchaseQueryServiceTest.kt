package com.homework.comics.purchase

import com.homework.comics.common.domain.Price
import com.homework.comics.member.domain.Member
import com.homework.comics.purchase.application.PurchaseQueryService
import com.homework.comics.purchase.application.impl.PurchaseQueryServiceImpl
import com.homework.comics.purchase.domain.Purchase
import com.homework.comics.purchase.repository.PurchaseRepository
import com.homework.comics.work.domain.Work
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.Optional

@SpringBootTest
class PurchaseQueryServiceTest {
    private lateinit var purchaseRepository: PurchaseRepository
    private lateinit var purchaseQueryService: PurchaseQueryService

    @BeforeEach
    fun setup() {
        purchaseRepository = mock(PurchaseRepository::class.java)
        purchaseQueryService = PurchaseQueryServiceImpl(purchaseRepository)
    }

    @Test
    fun `구매내역 조회 성공`() {
        // given
        val id = 1L
        val purchase = Purchase(
            id = id,
            member = Member(id = 1L),
            work = Work(id = 1L),
            paidPrice = Price(BigDecimal("1000"))
        )

        `when`(purchaseRepository.findById(id)).thenReturn(Optional.of(purchase))

        // when
        val result = purchaseQueryService.findById(id)

        // then
        assertEquals(purchase.id, result.id)
        assertEquals(purchase.paidPriceValue, result.paidPrice)
    }

    @Test
    fun `구매내역 조회 실패 - 존재하지 않는 구매내역`() {
        // given
        val id = 999L
        `when`(purchaseRepository.findById(id)).thenReturn(Optional.empty())

        // when & then
        assertThrows<NoSuchElementException>("존재하지 않는 구매내역입니다.") {
            purchaseQueryService.findById(id)
        }
    }
}
