package com.homework.comics.work.application.impl

import com.homework.comics.common.component.AuthenticationFacade
import com.homework.comics.common.exception.AdultOnlyAccessDeniedException
import com.homework.comics.work.application.WorkQueryService
import com.homework.comics.work.application.dto.WorkServiceResponse
import com.homework.comics.work.domain.Work
import com.homework.comics.work.repository.WorkRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class WorkQueryServiceImpl(
    private val workRepository: WorkRepository,
    private val authenticationFacade: AuthenticationFacade,
) : WorkQueryService {

    override fun fetchAll(): Iterable<Work> =
        workRepository.findAll()

    @Cacheable(cacheNames = ["work"], key = "#id + '::' + @authenticationFacade.isAdultVerified()")
    override fun findById(id: Long): WorkServiceResponse {
        val work = fetchById(id)

        if (work.isAdultOnly && !authenticationFacade.isAdultVerified()) {
            throw AdultOnlyAccessDeniedException("성인 인증이 필요한 작품입니다.")
        }

        return WorkServiceResponse.of(work)
    }

    override fun fetchById(id: Long): Work =
        workRepository.findById(id)
            .orElseThrow { NoSuchElementException("존재하지 않는 작품입니다. - $id") }

}
