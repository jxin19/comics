package com.homework.comics.purchaseStats.domain

import com.homework.comics.work.domain.Work
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "purchase_stats_mv")
class PurchaseStats(
    @Id
    @Column(name = "work_id")
    val workId: Long,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id", updatable = false)
    val work: Work,

    @Column(name = "total_purchases", nullable = false)
    val totalPurchases: Long = 0,

    @Column(name = "total_revenue", nullable = false)
    val totalRevenue: BigDecimal = BigDecimal.ZERO,

    @Column(name = "last_updated_at", nullable = false)
    val lastUpdatedAt: Instant
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PurchaseStats

        if (totalPurchases != other.totalPurchases) return false
        if (work != other.work) return false
        if (totalRevenue != other.totalRevenue) return false
        if (lastUpdatedAt != other.lastUpdatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = totalPurchases.hashCode()
        result = 31 * result + work.hashCode()
        result = 31 * result + totalRevenue.hashCode()
        result = 31 * result + lastUpdatedAt.hashCode()
        return result
    }

    override fun toString(): String {
        return "PurchaseStats(work=$work, totalPurchases=$totalPurchases, totalRevenue=$totalRevenue, lastUpdatedAt=$lastUpdatedAt)"
    }
}
