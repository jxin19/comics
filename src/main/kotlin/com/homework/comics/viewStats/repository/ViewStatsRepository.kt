package com.homework.comics.viewStats.repository

import com.homework.comics.viewStats.domain.ViewStats
import com.homework.comics.viewStats.repository.custom.ViewStatsCustomRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface ViewStatsRepository : MongoRepository<ViewStats, String>, ViewStatsCustomRepository {
    fun findByWorkId(workId: Long): Optional<ViewStats>
    fun findByOrderByTotalViewsDesc(pageable: Pageable): List<ViewStats>
    override fun incrementTotalViews(workIdToIncrementalViews: Map<Long, Long>)
}
