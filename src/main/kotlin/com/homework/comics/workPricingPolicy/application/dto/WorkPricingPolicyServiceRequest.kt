package com.homework.comics.workPricingPolicy.application.dto

import com.homework.comics.common.domain.Price
import com.homework.comics.workPricingPolicy.domain.WorkPricingPolicy
import com.homework.comics.work.domain.Work
import java.math.BigDecimal
import java.time.Instant

data class WorkPricingPolicyServiceRequest(
    val workId: Long,
    val isFree: Boolean?,
    val price: BigDecimal?,
    val startAt: Instant,
    val endAt: Instant?,
) {
    fun toEntity(work: Work) = WorkPricingPolicy(
        work = work,
        _isFree = isFree ?: false,
        price = price?.let { Price(it) },
        startDate = startAt,
        endDate = endAt
    )
}
