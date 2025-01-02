package com.homework.comics.workPricingPolicy.application.impl

import com.homework.comics.work.application.WorkQueryService
import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyCommandService
import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyQueryService
import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceRequest
import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceResponse
import com.homework.comics.workPricingPolicy.repository.WorkPricingPolicyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WorkPricingPolicyCommandServiceImpl(
    private val workPricingPolicyRepository: WorkPricingPolicyRepository,
    private val workPricingPolicyQueryService: WorkPricingPolicyQueryService,
    private val workQueryService: WorkQueryService
) : WorkPricingPolicyCommandService {

    override fun create(request: WorkPricingPolicyServiceRequest): WorkPricingPolicyServiceResponse {
        val work = workQueryService.fetchById(request.workId)
        val workPricingPolicy = request.toEntity(work)
        val savedWorkPricingPolicy = workPricingPolicyRepository.save(workPricingPolicy)

        return WorkPricingPolicyServiceResponse.of(savedWorkPricingPolicy)
    }

    override fun update(id: Long, request: WorkPricingPolicyServiceRequest): WorkPricingPolicyServiceResponse {
        val workPricingPolicy = workPricingPolicyQueryService.fetchById(id)
        val updateWorkPricingPolicy = request.toEntity(workPricingPolicy.workEntity)
        workPricingPolicy.update(updateWorkPricingPolicy)

        return WorkPricingPolicyServiceResponse.of(workPricingPolicy)
    }

    override fun delete(id: Long) {
        workPricingPolicyRepository.deleteById(id)
    }

}
