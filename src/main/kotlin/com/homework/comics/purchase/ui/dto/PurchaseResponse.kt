package com.homework.comics.purchase.ui.dto

import com.homework.comics.purchase.application.dto.PurchaseServiceResponse
import java.math.BigDecimal
import java.time.Instant

data class PurchaseResponse(
    val id: Long = 0,
    val memberId: Long = 0,
    val workId: Long = 0,
    val paidPrice: BigDecimal = BigDecimal.ZERO,
    val purchasedDateTime: Instant = Instant.now(),
) {
    companion object {
        fun of(purchaseServiceResponse: PurchaseServiceResponse) = PurchaseResponse(
            id = purchaseServiceResponse.id,
            memberId = purchaseServiceResponse.memberId,
            workId = purchaseServiceResponse.workId,
            paidPrice = purchaseServiceResponse.paidPrice,
            purchasedDateTime = purchaseServiceResponse.purchasedDateTime
        )
    }
}
