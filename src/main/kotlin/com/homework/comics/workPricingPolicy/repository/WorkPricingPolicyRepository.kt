package com.homework.comics.workPricingPolicy.repository

import com.homework.comics.workPricingPolicy.domain.WorkPricingPolicy
import com.homework.comics.workPricingPolicy.repository.custom.WorkPricingPolicyCustomRepository
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.repository.CrudRepository
import java.util.*

interface WorkPricingPolicyRepository : CrudRepository<WorkPricingPolicy, Long>, WorkPricingPolicyCustomRepository {

    @EntityGraph(attributePaths = ["work"])
    override fun findById(id: Long): Optional<WorkPricingPolicy>

    override fun findCurrentPolicy(workId: Long): Optional<WorkPricingPolicy>

}
