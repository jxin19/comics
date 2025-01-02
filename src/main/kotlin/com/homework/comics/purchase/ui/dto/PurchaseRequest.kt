package com.homework.comics.purchase.ui.dto

import com.homework.comics.purchase.application.dto.PurchaseServiceRequest
import jakarta.validation.constraints.NotNull

data class PurchaseRequest(
    @field:NotNull(message = "회원번호를 입력해주세요.")
    val memberId: Long,

    @field:NotNull(message = "작품번호를 입력해주세요.")
    val workId: Long,
) {
    fun toServiceDto() = PurchaseServiceRequest(
        memberId = memberId,
        workId = workId,
    )
}
