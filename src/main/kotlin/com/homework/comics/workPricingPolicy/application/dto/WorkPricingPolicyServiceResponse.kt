package com.homework.comics.workPricingPolicy.application.dto

import com.homework.comics.work.application.dto.WorkServiceResponse
import com.homework.comics.workPricingPolicy.domain.WorkPricingPolicy
import java.math.BigDecimal
import java.time.Instant

data class WorkPricingPolicyServiceResponse(
    val id: Long = 0,
    val workId: Long = 0,
    val workServiceResponse: WorkServiceResponse = WorkServiceResponse(),
    val free: Boolean = false,
    val price: BigDecimal = BigDecimal.ZERO,
    val startAt: Instant = Instant.MIN,
    val endAt: Instant? = null,
) {
    companion object {
        fun of(workPricingPolicy: WorkPricingPolicy) = WorkPricingPolicyServiceResponse(
            id = workPricingPolicy.id,
            workId = workPricingPolicy.workId,
            workServiceResponse = WorkServiceResponse.of(workPricingPolicy.workEntity),
            free = workPricingPolicy.isFree,
            price = workPricingPolicy.priceValue,
            startAt = workPricingPolicy.startAt,
            endAt = workPricingPolicy.endAt,
        )
    }
}
