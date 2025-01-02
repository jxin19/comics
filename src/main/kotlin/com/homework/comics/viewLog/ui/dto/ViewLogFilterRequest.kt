package com.homework.comics.viewLog.ui.dto

import com.homework.comics.viewLog.application.dto.ViewLogFilterServiceRequest
import jakarta.validation.constraints.NotNull
import java.time.Instant

data class ViewLogFilterRequest(
    @field:NotNull(message = "작품번호를 입력해주세요.")
    val workId: Long,
    val memberId: Int? = null,
    val startDate: Instant?,
    val endDate: Instant?,
    val page: Int = 1,
    val size: Int = 20
) {
    fun toServiceDto() = ViewLogFilterServiceRequest(
        workId = workId,
        memberId = memberId,
        startDate = startDate,
        endDate = endDate,
        page = page,
        size = size
    )
}
