package com.homework.comics.workPricingPolicy.repository.custom

import com.homework.comics.workPricingPolicy.domain.WorkPricingPolicy
import java.util.*

interface WorkPricingPolicyCustomRepository {
    fun findCurrentPolicy(workId: Long): Optional<WorkPricingPolicy>
}
