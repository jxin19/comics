package com.homework.comics.viewStats.application.impl

import com.homework.comics.viewLog.application.ViewLogQueryService
import com.homework.comics.viewLog.domain.ViewLog
import com.homework.comics.viewStats.application.ViewStatsCommandService
import com.homework.comics.viewStats.application.ViewStatsQueryService
import com.homework.comics.viewStats.application.extension.toWorkInfo
import com.homework.comics.viewStats.domain.ViewStats
import com.homework.comics.viewStats.repository.ViewStatsRepository
import com.homework.comics.work.domain.Work
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ViewStatsCommandServiceImpl(
    private val viewStatsRepository: ViewStatsRepository,
    private val viewStatsQueryService: ViewStatsQueryService,
    private val viewLogQueryService: ViewLogQueryService,
    private val redisTemplate: RedisTemplate<String, String>
) : ViewStatsCommandService {
    private companion object {
        const val LATEST_VIEW_LOG_ID_KEY = "latest-view-log-id"
        const val BATCH_SIZE: Int = 1_000
    }

    override fun batchCreate(works: Iterable<Work>) {
        val viewStatsList = works.map { work ->
            ViewStats(
                workId = work.id,
                workInfo = work.toWorkInfo(),
                lastUpdatedAt = Instant.now()
            )
        }
        viewStatsRepository.saveAll(viewStatsList)
    }

    override fun create(work: Work) {
        val viewStats = ViewStats(
            workId = work.id,
            workInfo = work.toWorkInfo(),
            lastUpdatedAt = Instant.now()
        )
        viewStatsRepository.save(viewStats)
    }

    override fun update(work: Work) {
        val viewStats = viewStatsQueryService.fetchByWorkId(work.id)
        viewStats.updateWorkInfo(work.toWorkInfo())
        viewStatsRepository.save(viewStats)
    }

    /**
     * 작품 조회수를 통계 데이터로 업데이트
     *
     * 1. Redis에서 마지막으로 처리한 조회 로그 ID를 조회
     * 2. 해당 ID 이후의 로그를 배치(BATCH_SIZE) 단위로 조회
     * 3. 작품별로 조회수를 집계하여 MongoDB에 Bulk Update
     * 4. 마지막으로 처리된 로그 ID를 Redis에 저장
     */
    @CacheEvict(cacheNames = ["topViewedWorks"])
    override fun loadLatestViewLogs() {
        var lastViewLogId = redisTemplate.opsForValue().get(LATEST_VIEW_LOG_ID_KEY)
        var maxLogId = lastViewLogId

        while (true) {
            val viewLogs = viewLogQueryService.fetchViewsAfterLogId(lastViewLogId, BATCH_SIZE)
            if (viewLogs.isEmpty()) break

            maxLogId = processViewLogsBatch(viewLogs)
            lastViewLogId = maxLogId

            if (viewLogs.size < BATCH_SIZE) break
        }

        maxLogId?.let { redisTemplate.opsForValue().set(LATEST_VIEW_LOG_ID_KEY, it) }
    }

    private fun processViewLogsBatch(viewLogs: List<ViewLog>): String {
        val workIdToIncrementalViews = viewLogs.groupBy { it.workId }
            .mapValues { it.value.size.toLong() }
        viewStatsRepository.incrementTotalViews(workIdToIncrementalViews)

        return viewLogs.maxOf { it.id!! }
    }

}
