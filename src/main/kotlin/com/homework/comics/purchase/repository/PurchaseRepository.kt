package com.homework.comics.purchase.repository

import com.homework.comics.purchase.domain.Purchase
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import java.util.*

interface PurchaseRepository : CrudRepository<Purchase, Long> {

    @EntityGraph(attributePaths = ["member", "work"])
    override fun findById(id: Long): Optional<Purchase>

    @EntityGraph(attributePaths = ["member", "work"])
    fun findByMember_IdOrderByPurchasedAtDesc(memberId: Long): List<Purchase>

    @EntityGraph(attributePaths = ["member", "work"])
    fun findByWork_IdOrderByPurchasedAtDesc(workId: Long): List<Purchase>
}
