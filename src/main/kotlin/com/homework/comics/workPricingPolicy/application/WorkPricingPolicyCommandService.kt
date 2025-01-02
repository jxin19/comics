package com.homework.comics.workPricingPolicy.application

import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceRequest
import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceResponse

interface WorkPricingPolicyCommandService {
    fun create(request: WorkPricingPolicyServiceRequest): WorkPricingPolicyServiceResponse
    fun update(id: Long, request: WorkPricingPolicyServiceRequest): WorkPricingPolicyServiceResponse
    fun delete(id: Long)
}
