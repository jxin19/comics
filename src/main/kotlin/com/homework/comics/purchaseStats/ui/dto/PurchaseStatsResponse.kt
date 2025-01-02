package com.homework.comics.purchaseStats.ui.dto

import com.homework.comics.purchaseStats.application.dto.PurchaseStatsServiceResponse
import com.homework.comics.work.ui.dto.WorkResponse
import java.math.BigDecimal

data class PurchaseStatsResponse(
    val workResponse: WorkResponse,
    val totalPurchases: Long,
    val totalRevenue: BigDecimal
) {
    companion object {
        fun of(purchaseStatsServiceResponse: PurchaseStatsServiceResponse) = PurchaseStatsResponse(
            workResponse = WorkResponse.of(purchaseStatsServiceResponse.workServiceResponse),
            totalPurchases = purchaseStatsServiceResponse.totalPurchases,
            totalRevenue = purchaseStatsServiceResponse.totalRevenue,
        )
    }
}
