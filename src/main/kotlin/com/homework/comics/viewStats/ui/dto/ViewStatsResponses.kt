package com.homework.comics.viewStats.ui.dto

import com.homework.comics.viewStats.application.dto.ViewStatsServiceResponses

data class ViewStatsResponses(
    val list: List<ViewStatsResponse>,
) {
    companion object {
        fun of(viewStatsServiceResponses: ViewStatsServiceResponses) = ViewStatsResponses(
            viewStatsServiceResponses.list.map { ViewStatsResponse.of(it) }
        )
    }
}
