package com.homework.comics.work.ui.dto

import com.homework.comics.work.application.dto.WorkServiceResponse

data class WorkResponse(
    val id: Long,
    val title: String,
    val description: String,
    val author: String,
    val active: Boolean,
    val adultOnly: Boolean,
) {
    companion object {
        fun of(workServiceResponse: WorkServiceResponse) = WorkResponse(
            id = workServiceResponse.id,
            title = workServiceResponse.title,
            description = workServiceResponse.description,
            author = workServiceResponse.author,
            active = workServiceResponse.active,
            adultOnly = workServiceResponse.adultOnly
        )
    }
}
