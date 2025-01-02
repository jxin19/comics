package com.homework.comics.viewStats.domain

import jakarta.persistence.*
import org.springframework.data.mongodb.core.index.IndexDirection
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.Instant

@Document(collection = "view_stats")
class ViewStats(
    @Id
    val id: String? = null,

    @Field("work_id")
    @Indexed(unique = true)
    val workId: Long,

    @Field("work")
    private var workInfo: WorkInfo,

    @Field("total_views")
    @Indexed(direction = IndexDirection.DESCENDING)
    private var totalViews: Long = 0,

    @Field("last_updated_at")
    private var lastUpdatedAt: Instant = Instant.now()
) {
    data class WorkInfo(
        val id: Long,
        val title: String,
        val description: String,
        val author: String,
        val isActive: Boolean,
        val isAdultOnly: Boolean
    )

    val work: WorkInfo
        get() = workInfo

    val totalViewCount: Long
        get() = totalViews

    val lastUpdatedDateTime
        get() = lastUpdatedAt

    fun updateWorkInfo(workInfo: WorkInfo) {
        this.workInfo = workInfo
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ViewStats

        if (workId != other.workId) return false
        if (totalViews != other.totalViews) return false
        if (id != other.id) return false
        if (lastUpdatedAt != other.lastUpdatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workId.hashCode()
        result = 31 * result + totalViews.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + lastUpdatedAt.hashCode()
        return result
    }

    override fun toString(): String {
        return "ViewStats(id=$id, workId=$workId, totalViews=$totalViews, lastUpdatedAt=$lastUpdatedAt)"
    }

}
