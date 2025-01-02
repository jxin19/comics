package com.homework.comics.viewStats.scheduler

import com.homework.comics.viewStats.application.ViewStatsCommandService
import com.homework.comics.viewStats.application.ViewStatsQueryService
import com.homework.comics.work.application.WorkQueryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableScheduling
class ViewStatsSchedulerConfig {
    @Bean
    fun viewStatsScheduler(
        viewStatsQueryService: ViewStatsQueryService,
        workQueryService: WorkQueryService,
        viewStatsCommandService: ViewStatsCommandService
    ) =
        ViewStatsScheduler(viewStatsQueryService, workQueryService, viewStatsCommandService)
}

