package com.homework.comics.viewLog.repository.custom

import com.homework.comics.viewLog.application.dto.ViewLogFilterServiceRequest
import com.homework.comics.viewLog.domain.ViewLog

interface ViewLogCustomRepository {
    fun findByFilter(request: ViewLogFilterServiceRequest): List<ViewLog>
    fun findViewsAfterLogId(id: String?, batchSize: Int): List<ViewLog>
    fun bulkInsert(list: List<ViewLog>)
}
