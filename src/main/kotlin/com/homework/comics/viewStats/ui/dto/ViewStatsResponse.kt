package com.homework.comics.viewStats.ui.dto

import com.homework.comics.viewStats.application.dto.ViewStatsServiceResponse
import com.homework.comics.work.ui.dto.WorkResponse

data class ViewStatsResponse(
    val workResponse: WorkResponse,
    val totalViews: Long,
) {
    companion object {
        fun of(viewStatsServiceResponse: ViewStatsServiceResponse) = ViewStatsResponse(
            workResponse = WorkResponse.of(viewStatsServiceResponse.workServiceResponse),
            totalViews = viewStatsServiceResponse.totalViews,
        )
    }
}
