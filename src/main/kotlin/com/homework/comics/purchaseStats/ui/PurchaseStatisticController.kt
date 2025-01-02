package com.homework.comics.purchaseStats.ui

import com.homework.comics.purchaseStats.application.PurchaseStatsQueryService
import com.homework.comics.purchaseStats.ui.dto.PurchaseStatsResponses
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "구매 통계 API")
@RestController
@RequestMapping("/purchase-stats")
class PurchaseStatisticController(
    private val purchaseStatsQueryService: PurchaseStatsQueryService
) {

    @GetMapping("/top-works")
    @Operation(summary = "인기 구매 작품 조회")
    fun findTopPurchasedWorks(
        @RequestParam(defaultValue = "10") limit: Int
    ): PurchaseStatsResponses {
        val response = purchaseStatsQueryService.findTopPurchasedWorks(limit)
        return PurchaseStatsResponses.of(response)
    }

}
