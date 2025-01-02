package com.homework.comics.purchaseStats.repository

import com.homework.comics.purchaseStats.domain.PurchaseStats
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PurchaseStatsRepository : JpaRepository<PurchaseStats, Long> {
    @Query("""
        SELECT p 
        FROM PurchaseStats p 
        JOIN FETCH p.work w 
        WHERE w._isActive = true
        ORDER BY p.totalPurchases DESC, p.totalRevenue DESC
    """)
    fun findTopPurchasedWorks(pageable: Pageable): List<PurchaseStats>
}
