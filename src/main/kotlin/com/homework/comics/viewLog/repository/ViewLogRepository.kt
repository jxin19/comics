package com.homework.comics.viewLog.repository

import com.homework.comics.viewLog.application.dto.ViewLogFilterServiceRequest
import com.homework.comics.viewLog.domain.ViewLog
import com.homework.comics.viewLog.repository.custom.ViewLogCustomRepository
import org.springframework.data.mongodb.repository.MongoRepository

interface ViewLogRepository : MongoRepository<ViewLog, Long>, ViewLogCustomRepository {
    override fun findByFilter(request: ViewLogFilterServiceRequest): List<ViewLog>
    override fun findViewsAfterLogId(id: String?, batchSize: Int): List<ViewLog>
    fun deleteByWorkId(workId: Long)
    override fun bulkInsert(list: List<ViewLog>)
}
