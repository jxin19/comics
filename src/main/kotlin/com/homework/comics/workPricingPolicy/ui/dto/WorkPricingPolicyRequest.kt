package com.homework.comics.workPricingPolicy.ui.dto

import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceRequest
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.Instant

data class WorkPricingPolicyRequest(
    @field:NotNull(message = "작품번호를 입력해주세요.")
    val workId: Long,

    val isFree: Boolean?,

    val price: BigDecimal?,

    @field:NotNull(message = "정책시작일을 입력해주세요.")
    val startAt: Instant,

    val endAt: Instant?,
) {
    fun toServiceDto() = WorkPricingPolicyServiceRequest(
        workId = workId,
        isFree = isFree,
        price = price,
        startAt = startAt,
        endAt = endAt
    )
}
