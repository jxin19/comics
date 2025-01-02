package com.homework.comics.viewStats.application

import com.homework.comics.viewStats.application.dto.ViewStatsServiceResponses
import com.homework.comics.viewStats.domain.ViewStats

interface ViewStatsQueryService {
    fun existDocument(): Boolean
    fun fetchByWorkId(workId: Long): ViewStats
    fun findTopViewedWorks(limit: Int = 10): ViewStatsServiceResponses
}
