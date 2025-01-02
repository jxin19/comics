package com.homework.comics.common.config

import com.homework.comics.viewLog.domain.ViewLog
import com.homework.comics.viewStats.domain.ViewStats
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.Index

@Configuration
class MongoIndexConfig {

    @Bean
    fun mongoIndexCreator(mongoTemplate: MongoTemplate): CommandLineRunner {
        return CommandLineRunner {
            // ViewLog 인덱스
            mongoTemplate.indexOps(ViewLog::class.java).ensureIndex(
                Index()
                    .on("work_id", Sort.Direction.DESC)
                    .on("viewed_at", Sort.Direction.DESC)
            )

            // ViewStats 인덱스
            mongoTemplate.indexOps(ViewStats::class.java).ensureIndex(
                Index()
                    .on("total_views", Sort.Direction.DESC)
                    .background()
            )
        }
    }
}
