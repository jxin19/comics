package com.homework.comics.workPricingPolicy.application

import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceResponse
import com.homework.comics.workPricingPolicy.domain.WorkPricingPolicy

interface WorkPricingPolicyQueryService {
    fun findById(id: Long): WorkPricingPolicyServiceResponse
    fun fetchById(id: Long): WorkPricingPolicy
    fun findCurrentPolicyByWorkId(workId: Long): WorkPricingPolicyServiceResponse
}
