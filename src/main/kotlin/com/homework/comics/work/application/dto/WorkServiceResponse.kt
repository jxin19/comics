package com.homework.comics.work.application.dto

import com.homework.comics.viewStats.domain.ViewStats
import com.homework.comics.work.domain.Work

data class WorkServiceResponse(
    val id: Long = 0,
    val title: String = "",
    val description: String = "",
    val author: String = "",
    val active: Boolean = true,
    val adultOnly: Boolean = false,
) {
    companion object {
        fun of(work: Work) = WorkServiceResponse(
            id = work.id,
            title = work.titleValue,
            description = work.descriptionValue,
            author = work.authorValue,
            active = work.isActive,
            adultOnly = work.isAdultOnly
        )
        fun of(workInfo: ViewStats.WorkInfo) = WorkServiceResponse(
            id = workInfo.id,
            title = workInfo.title,
            description = workInfo.description,
            author = workInfo.author,
            active = workInfo.isActive,
            adultOnly = workInfo.isAdultOnly
        )
    }
}
