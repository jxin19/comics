package com.homework.comics.workPricingPolicy.application.impl

import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyQueryService
import com.homework.comics.workPricingPolicy.application.dto.WorkPricingPolicyServiceResponse
import com.homework.comics.workPricingPolicy.domain.WorkPricingPolicy
import com.homework.comics.workPricingPolicy.repository.WorkPricingPolicyRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WorkPricingPolicyQueryServiceImpl(
    private val workPricingPolicyRepository: WorkPricingPolicyRepository
) : WorkPricingPolicyQueryService {

    override fun findById(id: Long): WorkPricingPolicyServiceResponse =
        WorkPricingPolicyServiceResponse.of(fetchById(id))

    override fun fetchById(id: Long): WorkPricingPolicy =
        workPricingPolicyRepository.findById(id)
            .orElseThrow { NoSuchElementException("존재하지 않는 가격정책입니다. - $id") }

    override fun findCurrentPolicyByWorkId(workId: Long): WorkPricingPolicyServiceResponse =
        WorkPricingPolicyServiceResponse.of(
            workPricingPolicyRepository.findCurrentPolicy(workId)
                .orElseThrow { NoSuchElementException("존재하지 않는 가격정책입니다. - $workId") }
        )

}
