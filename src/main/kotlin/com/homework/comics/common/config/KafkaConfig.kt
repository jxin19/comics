package com.homework.comics.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import com.homework.comics.viewLog.application.dto.ViewAvro

@Configuration
class KafkaConfig {

    companion object {
        const val MESSAGE_CONSUMER_GROUP = "view-message-processor"
        const val MESSAGE_TOPIC = "view.messages"
        const val DLQ_TOPIC = "view.messages.dlq"
        const val MAX_RETRY_ATTEMPTS = 3
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, ViewAvro>): KafkaTemplate<String, ViewAvro> {
        return KafkaTemplate(producerFactory)
    }

}
