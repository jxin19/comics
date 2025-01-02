package com.homework.comics.work.application.impl

import com.homework.comics.viewLog.application.ViewLogCommandService
import com.homework.comics.viewStats.application.ViewStatsCommandService
import com.homework.comics.work.application.WorkCommandService
import com.homework.comics.work.application.WorkQueryService
import com.homework.comics.work.application.dto.WorkServiceRequest
import com.homework.comics.work.application.dto.WorkServiceResponse
import com.homework.comics.work.repository.WorkRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WorkCommandServiceImpl(
    private val workRepository: WorkRepository,
    private val workQueryService: WorkQueryService,
    private val viewStatsCommandService: ViewStatsCommandService,
    private val viewLogCommandService: ViewLogCommandService,
) : WorkCommandService {

    @CachePut(cacheNames = ["work"], key = "#result.id")
    override fun create(request: WorkServiceRequest): WorkServiceResponse {
        val work = request.toEntity()
        val savedWork = workRepository.save(work)

        viewStatsCommandService.create(savedWork) // 조회통계 기본데이터 추가

        return WorkServiceResponse.of(savedWork)
    }

    @CachePut(cacheNames = ["work"], key = "#id")
    override fun update(id: Long, request: WorkServiceRequest): WorkServiceResponse {
        val work = workQueryService.fetchById(id)
        work.update(request.toEntity())

        viewStatsCommandService.update(work) // 조회통계 기본데이터 갱신

        return WorkServiceResponse.of(work)
    }

    @CacheEvict(cacheNames = ["work"], key = "#id")
    override fun delete(id: Long) {
        workRepository.deleteById(id)
        viewLogCommandService.deleteByWorkId(id) // 조회로그 삭제
    }

}
