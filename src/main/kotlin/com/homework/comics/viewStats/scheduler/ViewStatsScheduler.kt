package com.homework.comics.viewStats.scheduler

import com.homework.comics.viewStats.application.ViewStatsCommandService
import com.homework.comics.viewStats.application.ViewStatsQueryService
import com.homework.comics.work.application.WorkQueryService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ViewStatsScheduler(
    private val viewStatsQueryService: ViewStatsQueryService,
    private val workQueryService: WorkQueryService,
    private val viewStatsCommandService: ViewStatsCommandService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${comics.init-view-log-stats}")
    private val initData: Boolean = false

    @EventListener(ApplicationReadyEvent::class)
    fun initializeStats() {
        if (initData) {
            createBaseViewStats()
            loadLatestViewLogs()
        } else {
            logger.info("Skipping initial view log stats refresh due to configuration")
        }
    }

    /**
     * 과제 검수를 위해 mongo db에 초기 데이터를 생성하기 위한 기능
     * 이미 초기 데이터가 있다면 실행하지 않도록 제어
     */
    private fun createBaseViewStats() {
        val existsDocument = viewStatsQueryService.existDocument()

        if (!existsDocument) {
            val works = workQueryService.fetchAll()
            viewStatsCommandService.batchCreate(works)
        }
    }

    /**
     * 조회이력 통계를 갱신하기 위한 스케줄러
     */
    @Scheduled(cron = "0 */15 * * * *")
    fun loadLatestViewLogs() {
        try {
            logger.info("Start load latest view logs")
            viewStatsCommandService.loadLatestViewLogs()
            logger.info("Completed load latest view logs")
        } catch (e: Exception) {
            logger.info("Failed to load latest view logs")
        }
    }
}

