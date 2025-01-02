package com.homework.comics.workPricingPolicy.repository.custom

import com.homework.comics.workPricingPolicy.domain.QWorkPricingPolicy
import com.homework.comics.workPricingPolicy.domain.WorkPricingPolicy
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import java.time.Instant
import java.util.Optional

class WorkPricingPolicyCustomRepositoryImpl(
    private val em: EntityManager,
    private val queryFactory: JPAQueryFactory = JPAQueryFactory(em)
) : WorkPricingPolicyCustomRepository {

    private val workPricingPolicy = QWorkPricingPolicy.workPricingPolicy

    override fun findCurrentPolicy(workId: Long): Optional<WorkPricingPolicy> {
        return Optional.ofNullable(
            queryFactory
                .selectFrom(workPricingPolicy)
                .join(workPricingPolicy.work).fetchJoin()
                .where(
                    workPricingPolicy.work.id.eq(workId),
                    workPricingPolicy.startDate.loe(Instant.now()),
                    workPricingPolicy.endDate.isNull()
                        .or(workPricingPolicy.endDate.goe(Instant.now()))
                )
                .orderBy(
                    workPricingPolicy._isFree.desc(),
                    workPricingPolicy.startDate.desc(),
                    workPricingPolicy.id.desc()
                )
                .limit(1)
                .fetchOne()
        )
    }

}
