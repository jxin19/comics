package com.homework.comics.viewLog.application.impl

import com.homework.comics.common.config.KafkaConfig
import com.homework.comics.viewLog.application.ViewLogCommandService
import com.homework.comics.viewLog.application.dto.ViewAvro
import com.homework.comics.viewLog.domain.ViewLog
import com.homework.comics.viewLog.repository.ViewLogRepository
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ViewLogCommandServiceImpl(
    private val kafkaTemplate: KafkaTemplate<String, ViewAvro>,
    private val viewLogRepository: ViewLogRepository,
) : ViewLogCommandService {
    override fun create(workId: Long, memberId: Long) {
        kafkaTemplate.send(
            KafkaConfig.MESSAGE_TOPIC,
            workId.toString(),
            ViewAvro(workId, memberId, Instant.now())
        )
    }

    @KafkaListener(
        topics = [KafkaConfig.MESSAGE_TOPIC],
        groupId = KafkaConfig.MESSAGE_CONSUMER_GROUP,
    )
    @Retryable(
        value = [Exception::class],
        maxAttempts = KafkaConfig.MAX_RETRY_ATTEMPTS,
        backoff = Backoff(delay = 1000, multiplier = 2.0, maxDelay = 6000)
    )
    fun consumeMessage(newMessages: List<ConsumerRecord<String, ViewAvro>>, acknowledgment: Acknowledgment) {
        val viewLogs = newMessages.map {
            val viewAvro = it.value()
            ViewLog(
                workId = viewAvro.workId,
                memberId = viewAvro.memberId,
                viewedAt = viewAvro.viewedAt
            )
        }

        viewLogRepository.bulkInsert(viewLogs)

        acknowledgment.acknowledge()
    }

    override fun deleteByWorkId(workId: Long) {
        viewLogRepository.deleteByWorkId(workId)
    }

}
