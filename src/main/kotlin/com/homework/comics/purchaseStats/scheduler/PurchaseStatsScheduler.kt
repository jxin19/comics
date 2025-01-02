package com.homework.comics.purchaseStats.scheduler

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PurchaseStatsScheduler(
    private val jdbcTemplate: JdbcTemplate,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Value("\${comics.init-purchase-stats}")
    private val initData: Boolean = false

    @EventListener(ApplicationReadyEvent::class)
    fun initializeStats() {
        if (initData) {
            refresh()
        } else {
            logger.info("Skipping initial purchase stats refresh due to configuration")
        }
    }

    @Scheduled(cron = "0 */15 * * * *")
    fun refresh() {
        try {
            logger.info("Start refreshing purchase stats")
            jdbcTemplate.execute("SELECT refresh_purchase_stats_mv()")
            logger.info("Completed refreshing purchase stats")
        } catch (e: Exception) {
            logger.error("Failed to refresh purchase stats", e)
        }
    }

}
