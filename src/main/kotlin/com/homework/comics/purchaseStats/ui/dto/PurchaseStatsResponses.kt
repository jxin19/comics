package com.homework.comics.purchaseStats.ui.dto

import com.homework.comics.purchaseStats.application.dto.PurchaseStatsServiceResponses

data class PurchaseStatsResponses(
    val list: List<PurchaseStatsResponse>,
) {
    companion object {
        fun of(purchaseStatsServiceResponses: PurchaseStatsServiceResponses) = PurchaseStatsResponses(
            purchaseStatsServiceResponses.list.map { PurchaseStatsResponse.of(it) }
        )
    }
}
