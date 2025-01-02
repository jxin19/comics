package com.homework.comics.viewStats.ui

import com.homework.comics.viewStats.application.ViewStatsQueryService
import com.homework.comics.viewStats.ui.dto.ViewStatsResponses
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "구매 통계 API")
@RestController
@RequestMapping("/view-stats")
class ViewStatsController(
    private val viewStatsQueryService: ViewStatsQueryService
) {
    @GetMapping("/top-viewed")
    @Operation(summary = "조회수 기준 인기 작품 목록 조회")
    fun getTopViewedWorks(
        @RequestParam(required = false, defaultValue = "10") limit: Int
    ): ViewStatsResponses {
        val responses = viewStatsQueryService.findTopViewedWorks(limit)
        return ViewStatsResponses.of(responses)
    }
}
