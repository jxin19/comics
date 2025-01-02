package com.homework.comics.purchaseStats

import com.homework.comics.purchaseStats.application.dto.PurchaseStatsServiceResponses
import com.homework.comics.purchaseStats.application.impl.PurchaseStatsQueryServiceImpl
import com.homework.comics.purchaseStats.domain.PurchaseStats
import com.homework.comics.purchaseStats.repository.PurchaseStatsRepository
import com.homework.comics.work.domain.Work
import com.homework.comics.work.domain.property.Author
import com.homework.comics.work.domain.property.Description
import com.homework.comics.work.domain.property.WorkTitle
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import java.math.BigDecimal
import java.time.Instant

class PurchaseStatsQueryServiceTest {
    private lateinit var purchaseStatsRepository: PurchaseStatsRepository
    private lateinit var purchaseStatsQueryService: PurchaseStatsQueryServiceImpl

    @BeforeEach
    fun setUp() {
        purchaseStatsRepository = mockk()
        purchaseStatsQueryService = PurchaseStatsQueryServiceImpl(purchaseStatsRepository)
    }

    @Test
    fun `인기 구매 작품을 조회`() {
        // given
        val limit = 2
        val now = Instant.now()
        val purchaseStatsList = listOf(
            createPurchaseStats(
                workId = 1L,
                title = "인기작품1",
                description = "설명1",
                author = "작가1",
                totalPurchases = 100L,
                totalRevenue = BigDecimal("10000"),
                lastUpdatedAt = now
            ),
            createPurchaseStats(
                workId = 2L,
                title = "인기작품2",
                description = "설명2",
                author = "작가2",
                totalPurchases = 50L,
                totalRevenue = BigDecimal("5000"),
                lastUpdatedAt = now
            )
        )

        every { purchaseStatsRepository.findTopPurchasedWorks(PageRequest.of(0, limit)) } returns purchaseStatsList

        // when
        val result: PurchaseStatsServiceResponses = purchaseStatsQueryService.findTopPurchasedWorks(limit)

        // then
        assertEquals(2, result.list.size)

        with(result.list[0]) {
            assertEquals(1L, workServiceResponse.id)
            assertEquals("인기작품1", workServiceResponse.title)
            assertEquals("설명1", workServiceResponse.description)
            assertEquals("작가1", workServiceResponse.author)
            assertEquals(100L, totalPurchases)
            assertEquals(BigDecimal("10000"), totalRevenue)
        }

        with(result.list[1]) {
            assertEquals(2L, workServiceResponse.id)
            assertEquals("인기작품2", workServiceResponse.title)
            assertEquals("설명2", workServiceResponse.description)
            assertEquals("작가2", workServiceResponse.author)
            assertEquals(50L, totalPurchases)
            assertEquals(BigDecimal("5000"), totalRevenue)
        }
    }

    private fun createPurchaseStats(
        workId: Long,
        title: String,
        description: String,
        author: String,
        totalPurchases: Long,
        totalRevenue: BigDecimal,
        lastUpdatedAt: Instant
    ): PurchaseStats {
        val work = Work(
            id = workId,
            title = WorkTitle(title),
            description = Description(description),
            author = Author(author)
        )

        return PurchaseStats(
            workId = workId,
            work = work,
            totalPurchases = totalPurchases,
            totalRevenue = totalRevenue,
            lastUpdatedAt = lastUpdatedAt
        )
    }
}
