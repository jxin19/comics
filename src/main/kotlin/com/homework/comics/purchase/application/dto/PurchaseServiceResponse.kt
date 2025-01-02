package com.homework.comics.purchase.application.dto

import com.homework.comics.purchase.domain.Purchase
import java.math.BigDecimal
import java.time.Instant

data class PurchaseServiceResponse(
    val id: Long = 0,
    val memberId: Long = 0,
    val workId: Long = 0,
    val paidPrice: BigDecimal = BigDecimal.ZERO,
    val purchasedDateTime: Instant = Instant.now(),
) {
    companion object {
        fun of(purchase: Purchase) = PurchaseServiceResponse(
            id = purchase.id,
            memberId = purchase.memberId,
            workId = purchase.workId,
            paidPrice = purchase.paidPriceValue,
            purchasedDateTime = purchase.purchasedDateTime
        )
    }
}
