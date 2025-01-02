package com.homework.comics.work.application.dto

import com.homework.comics.work.domain.Work
import com.homework.comics.work.domain.property.Author
import com.homework.comics.work.domain.property.Description
import com.homework.comics.work.domain.property.WorkTitle

data class WorkServiceRequest(
    val title: String,
    val description: String?,
    val author: String?,
    val isActive: Boolean = true,
    val isAdultOnly: Boolean = false,
) {
    fun toEntity() = Work(
        title = WorkTitle(title),
        description = description?.let { Description(it) },
        author = author?.let { Author(it) },
        _isActive = isActive,
        _isAdultOnly = isAdultOnly
    )
}
