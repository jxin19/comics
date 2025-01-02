package com.homework.comics.purchaseStats.application

import com.homework.comics.purchaseStats.application.dto.PurchaseStatsServiceResponses

interface PurchaseStatsQueryService {
    fun findTopPurchasedWorks(limit: Int = 10): PurchaseStatsServiceResponses
}
