package com.homework.comics.viewStats.repository.custom

interface ViewStatsCustomRepository {
    fun incrementTotalViews(workIdToIncrementalViews: Map<Long, Long>)
}
