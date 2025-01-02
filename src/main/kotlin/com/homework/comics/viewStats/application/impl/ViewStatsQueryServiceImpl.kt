package com.homework.comics.viewStats.application.impl

import com.homework.comics.viewStats.application.ViewStatsQueryService
import com.homework.comics.viewStats.application.dto.ViewStatsServiceResponses
import com.homework.comics.viewStats.domain.ViewStats
import com.homework.comics.viewStats.repository.ViewStatsRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ViewStatsQueryServiceImpl(
    private val viewStatsRepository: ViewStatsRepository
): ViewStatsQueryService {

    override fun existDocument(): Boolean =
        viewStatsRepository.count() > 0

    override fun fetchByWorkId(workId: Long): ViewStats =
        viewStatsRepository.findByWorkId(workId)
            .orElseThrow { NoSuchElementException("존재하지 않는 작품입니다. - $workId") }

    @Cacheable(cacheNames = ["topViewedWorks"])
    override fun findTopViewedWorks(limit: Int): ViewStatsServiceResponses {
        val list = viewStatsRepository.findByOrderByTotalViewsDesc(PageRequest.of(0, limit))
        return ViewStatsServiceResponses.of(list)
    }

}
