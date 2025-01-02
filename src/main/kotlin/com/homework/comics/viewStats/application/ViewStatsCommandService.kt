package com.homework.comics.viewStats.application

import com.homework.comics.work.domain.Work

interface ViewStatsCommandService {
    fun batchCreate(works: Iterable<Work>)
    fun create(work: Work)
    fun update(work: Work)
    fun loadLatestViewLogs()
}
