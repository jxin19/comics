package com.homework.comics.purchaseStats.application.impl

import com.homework.comics.purchaseStats.application.PurchaseStatsQueryService
import com.homework.comics.purchaseStats.application.dto.PurchaseStatsServiceResponses
import com.homework.comics.purchaseStats.repository.PurchaseStatsRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PurchaseStatsQueryServiceImpl(
    private val purchaseStatsRepository: PurchaseStatsRepository
) : PurchaseStatsQueryService {
    @Cacheable(cacheNames = ["topPurchasedWorks"], key = "#limit")
    override fun findTopPurchasedWorks(limit: Int): PurchaseStatsServiceResponses {
        val purchaseStats = purchaseStatsRepository.findTopPurchasedWorks(PageRequest.of(0, limit))
        return PurchaseStatsServiceResponses.of(purchaseStats)
    }
}
