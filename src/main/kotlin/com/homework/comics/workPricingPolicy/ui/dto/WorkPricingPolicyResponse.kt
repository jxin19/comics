package com.homework.comics.workPricingPolicy.ui.dto

import com.homework.comics.work.ui.dto.WorkResponse
import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceResponse
import java.math.BigDecimal
import java.time.Instant

data class WorkPricingPolicyResponse(
    val id: Long = 0,
    val workId: Long = 0,
    val work: WorkResponse,
    val free: Boolean = false,
    val price: BigDecimal = BigDecimal.ZERO,
    val startAt: Instant = Instant.MIN,
    val endAt: Instant? = null,
) {
    companion object {
        fun of(workPricingPolicyServiceResponse: WorkPricingPolicyServiceResponse) = WorkPricingPolicyResponse(
            id = workPricingPolicyServiceResponse.id,
            workId = workPricingPolicyServiceResponse.workId,
            work = WorkResponse.of(workPricingPolicyServiceResponse.workServiceResponse),
            free = workPricingPolicyServiceResponse.free,
            price = workPricingPolicyServiceResponse.price,
            startAt = workPricingPolicyServiceResponse.startAt,
            endAt = workPricingPolicyServiceResponse.endAt,
        )
    }
}
