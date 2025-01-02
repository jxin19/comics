package com.homework.comics.viewLog.application.dto

import com.homework.comics.viewLog.domain.ViewLog
import java.time.Instant

data class ViewLogServiceResponse(
    val id: String?,
    val workId: Long,
    val memberId: Long,
    val viewedAt: Instant
) {
    companion object {
        fun of(viewLog: ViewLog) = ViewLogServiceResponse(
            id = viewLog.id,
            workId = viewLog.workId,
            memberId = viewLog.memberId,
            viewedAt = viewLog.viewedAt
        )
    }
}
