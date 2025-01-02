package com.homework.comics.work.ui.dto

import com.homework.comics.work.application.dto.WorkServiceRequest
import jakarta.validation.constraints.NotBlank

data class WorkRequest(
    @field:NotBlank(message = "작품명을 입력해주세요.")
    val title: String,

    val description: String?,

    @field:NotBlank(message = "작가명을 입력해주세요.")
    val author: String?,

    val isActive: Boolean = true,

    val isAdultOnly: Boolean = false,
) {
    fun toServiceDto() = WorkServiceRequest(
        title = title,
        description = description,
        author = author,
        isActive = isActive,
        isAdultOnly = isAdultOnly
    )
}
