package com.homework.comics.viewLog.domain

import jakarta.persistence.*
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.Sharded
import java.time.Instant

@Document(collection = "view_log")
@Sharded(shardKey = ["work_id", "viewed_at"])
class ViewLog(
    @Id
    val id: String? = null,

    @Field("work_id")
    val workId: Long = 0,

    @Field("member_id")
    val memberId: Long = 0,

    @Field("viewed_at")
    val viewedAt: Instant = Instant.now()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ViewLog

        if (workId != other.workId) return false
        if (memberId != other.memberId) return false
        if (id != other.id) return false
        if (viewedAt != other.viewedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workId.hashCode()
        result = 31 * result + memberId.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + viewedAt.hashCode()
        return result
    }

    override fun toString(): String {
        return "ViewLog(id=$id, workId=$workId, memberId=$memberId, viewedAt=$viewedAt)"
    }
}
