package com.homework.comics.purchaseStats.application.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.homework.comics.purchaseStats.domain.PurchaseStats
import com.homework.comics.work.application.dto.WorkServiceResponse
import java.math.BigDecimal

data class PurchaseStatsServiceResponse @JsonCreator constructor(
    @JsonProperty("workServiceResponse")
    val workServiceResponse: WorkServiceResponse = WorkServiceResponse(),

    @JsonProperty("totalPurchases")
    val totalPurchases: Long = 0,

    @JsonProperty("totalRevenue")
    val totalRevenue: BigDecimal = BigDecimal.ZERO,
) {
    companion object {
        fun of(purchaseStats: PurchaseStats) = PurchaseStatsServiceResponse(
            workServiceResponse = WorkServiceResponse.of(purchaseStats.work),
            totalPurchases = purchaseStats.totalPurchases,
            totalRevenue = purchaseStats.totalRevenue ?: BigDecimal.ZERO,
        )
    }
}
