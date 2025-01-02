package com.homework.comics.viewStats.application.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.homework.comics.viewStats.domain.ViewStats

data class ViewStatsServiceResponses @JsonCreator constructor(
    @JsonProperty("list")
    val list: List<ViewStatsServiceResponse>,
) {
    companion object {
        fun of(list: List<ViewStats>) = ViewStatsServiceResponses(
            list.map { ViewStatsServiceResponse.of(it) }
        )
    }
}
