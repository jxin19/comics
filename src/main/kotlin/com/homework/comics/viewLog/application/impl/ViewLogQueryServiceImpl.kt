package com.homework.comics.viewLog.application.impl

import com.homework.comics.viewLog.application.ViewLogQueryService
import com.homework.comics.viewLog.application.dto.ViewLogFilterServiceRequest
import com.homework.comics.viewLog.application.dto.ViewLogServiceResponses
import com.homework.comics.viewLog.domain.ViewLog
import com.homework.comics.viewLog.repository.ViewLogRepository
import org.springframework.stereotype.Service

@Service
class ViewLogQueryServiceImpl(
    private val viewLogRepository: ViewLogRepository
) : ViewLogQueryService {

    override fun findByFilter(viewLogFilterServiceRequest: ViewLogFilterServiceRequest): ViewLogServiceResponses {
        val viewLogs = viewLogRepository.findByFilter(viewLogFilterServiceRequest)
        return ViewLogServiceResponses.of(viewLogs)
    }

    override fun fetchViewsAfterLogId(viewLogId: String?, batchSize: Int): List<ViewLog> =
        viewLogRepository.findViewsAfterLogId(viewLogId, batchSize)
}
