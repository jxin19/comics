package com.homework.comics.viewLog.application

import com.homework.comics.viewLog.application.dto.ViewLogFilterServiceRequest
import com.homework.comics.viewLog.application.dto.ViewLogServiceResponses
import com.homework.comics.viewLog.domain.ViewLog

interface ViewLogQueryService {
    fun findByFilter(viewLogFilterServiceRequest: ViewLogFilterServiceRequest): ViewLogServiceResponses
    fun fetchViewsAfterLogId(viewLogId: String?, batchSize: Int): List<ViewLog>
}
