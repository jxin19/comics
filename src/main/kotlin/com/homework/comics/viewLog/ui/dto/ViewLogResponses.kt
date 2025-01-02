package com.homework.comics.viewLog.ui.dto

import com.homework.comics.viewLog.application.dto.ViewLogServiceResponses

data class ViewLogResponses(
    val list: List<ViewLogResponse>
) {
    companion object {
        fun of(viewLogServiceResponses: ViewLogServiceResponses) = ViewLogResponses(
            viewLogServiceResponses.list.map { ViewLogResponse.of(it) }
        )
    }
}
