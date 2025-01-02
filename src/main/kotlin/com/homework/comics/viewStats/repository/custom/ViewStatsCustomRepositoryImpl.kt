package com.homework.comics.viewStats.repository.custom

import com.homework.comics.viewStats.domain.ViewStats
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import java.time.Instant

class ViewStatsCustomRepositoryImpl(
    private val mongoTemplate: MongoTemplate
) : ViewStatsCustomRepository {
    override fun incrementTotalViews(workIdToIncrementalViews: Map<Long, Long>) {
        mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, ViewStats::class.java)
            .apply {
                workIdToIncrementalViews.forEach { (workId, incrementalViews) ->
                    updateOne(
                        Query(Criteria.where("work_id").`is`(workId)),
                        Update()
                            .inc("total_views", incrementalViews)
                            .set("last_updated_at", Instant.now())
                    )
                }
            }
            .execute()
    }
}
