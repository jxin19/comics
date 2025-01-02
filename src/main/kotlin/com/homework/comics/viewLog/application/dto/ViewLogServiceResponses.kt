package com.homework.comics.viewLog.application.dto

import com.homework.comics.viewLog.domain.ViewLog

data class ViewLogServiceResponses(
    val list: List<ViewLogServiceResponse>
) {
    companion object {
        fun of(viewLogs: List<ViewLog>) = ViewLogServiceResponses(
            viewLogs.map { ViewLogServiceResponse.of(it) }
        )
    }
}
