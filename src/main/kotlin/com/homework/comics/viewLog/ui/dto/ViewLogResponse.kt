package com.homework.comics.viewLog.ui.dto

import com.homework.comics.viewLog.application.dto.ViewLogServiceResponse
import java.time.Instant

data class ViewLogResponse(
    val id: String?,
    val workId: Long,
    val memberId: Long,
    val viewedAt: Instant
) {
    companion object {
        fun of(viewLogServiceResponse: ViewLogServiceResponse) = ViewLogResponse(
            id = viewLogServiceResponse.id,
            workId = viewLogServiceResponse.workId,
            memberId = viewLogServiceResponse.memberId,
            viewedAt = viewLogServiceResponse.viewedAt
        )
    }
}
