package com.homework.comics.viewLog.repository.custom

import com.homework.comics.viewLog.application.dto.ViewLogFilterServiceRequest
import com.homework.comics.viewLog.domain.ViewLog
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class ViewLogCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate,
) : ViewLogCustomRepository {
    override fun findByFilter(request: ViewLogFilterServiceRequest): List<ViewLog> {
        val query = Query()
            .addCriteria(Criteria.where("work_id").`is`(request.workId))
            .with(Sort.by(Sort.Direction.DESC, "viewed_at"))
            .with(PageRequest.of(request.page - 1, request.size))

        // 선택 조건: memberId
        request.memberId?.let { memberId ->
            query.addCriteria(Criteria.where("member_id").`is`(memberId))
        }

        // 선택 조건: 기간 검색
        if (request.startDate != null || request.endDate != null) {
            val viewedAtCriteria = Criteria.where("viewed_at")

            request.startDate?.let { startDate ->
                viewedAtCriteria.gte(startDate)
            }

            request.endDate?.let { endDate ->
                viewedAtCriteria.lte(endDate)
            }

            query.addCriteria(viewedAtCriteria)
        }

        return mongoTemplate.find(query, ViewLog::class.java)
    }

    override fun findViewsAfterLogId(id: String?, batchSize: Int): List<ViewLog> {
        val query = Query()
            .limit(batchSize)
            .with(Sort.by(Sort.Direction.ASC, "_id"))  // _id 기준으로 오름차순 정렬

        id?.let {
            query.addCriteria(Criteria.where("_id").gt(ObjectId(it)))
        }

        return mongoTemplate.find(query, ViewLog::class.java)
    }

    override fun bulkInsert(list: List<ViewLog>) {
        val operations = list.map {
            Document()
                .append("work_id", it.workId)
                .append("member_id", it.memberId)
                .append("viewed_at", it.viewedAt)
        }

        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ViewLog::class.java)
            .insert(operations)
            .execute()
    }
}
