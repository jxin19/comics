package com.homework.comics.purchaseStats.scheduler

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
class PurchaseStatsSchedulerConfig {

    @Bean
    fun purchaseStatsScheduler(jdbcTemplate: JdbcTemplate) = PurchaseStatsScheduler(jdbcTemplate)

}
