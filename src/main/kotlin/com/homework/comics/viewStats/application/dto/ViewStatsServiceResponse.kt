package com.homework.comics.viewStats.application.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.homework.comics.viewStats.domain.ViewStats
import com.homework.comics.work.application.dto.WorkServiceResponse

data class ViewStatsServiceResponse @JsonCreator constructor(
    @JsonProperty("workServiceResponse")
    val workServiceResponse: WorkServiceResponse = WorkServiceResponse(),

    @JsonProperty("totalViews")
    val totalViews: Long = 0,
) {
    companion object {
        fun of(viewStats: ViewStats) = ViewStatsServiceResponse(
            workServiceResponse = WorkServiceResponse.of(viewStats.work),
            totalViews = viewStats.totalViewCount,
        )
    }
}
