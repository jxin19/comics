package com.homework.comics.purchaseStats.application.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.homework.comics.purchaseStats.domain.PurchaseStats

data class PurchaseStatsServiceResponses @JsonCreator constructor(
    @JsonProperty("list")
    val list: List<PurchaseStatsServiceResponse>,
) {
    companion object {
        fun of(purchaseStats: List<PurchaseStats>) = PurchaseStatsServiceResponses(
            purchaseStats.map { PurchaseStatsServiceResponse.of(it) }
        )
    }
}
