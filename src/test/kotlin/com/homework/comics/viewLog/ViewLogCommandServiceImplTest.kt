package com.homework.comics.viewLog

import com.homework.comics.common.config.KafkaConfig
import com.homework.comics.viewLog.application.dto.ViewAvro
import com.homework.comics.viewLog.application.impl.ViewLogCommandServiceImpl
import com.homework.comics.viewLog.domain.ViewLog
import com.homework.comics.viewLog.repository.ViewLogRepository
import io.mockk.*
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import java.time.Instant

class ViewLogCommandServiceImplTest {
    private lateinit var kafkaTemplate: KafkaTemplate<String, ViewAvro>
    private lateinit var viewLogRepository: ViewLogRepository
    private lateinit var viewLogCommandService: ViewLogCommandServiceImpl
    private lateinit var acknowledgment: Acknowledgment

    @BeforeEach
    fun setUp() {
        kafkaTemplate = mockk(relaxed = true)
        viewLogRepository = mockk(relaxed = true)
        acknowledgment = mockk(relaxed = true)
        viewLogCommandService = ViewLogCommandServiceImpl(kafkaTemplate, viewLogRepository)
    }

    @Test
    fun `조회이력 메시지 전송`() {
        // given
        val workId = 1L
        val memberId = 2L
        val slot = slot<ViewAvro>()

        every {
            kafkaTemplate.send(
                KafkaConfig.MESSAGE_TOPIC,
                workId.toString(),
                capture(slot)
            )
        } returns mockk()

        // when
        viewLogCommandService.create(workId, memberId)

        // then
        verify {
            kafkaTemplate.send(
                KafkaConfig.MESSAGE_TOPIC,
                workId.toString(),
                any()
            )
        }
        assertEquals(workId, slot.captured.workId)
        assertEquals(memberId, slot.captured.memberId)
    }

    @Test
    fun `조회이력 메시지 수신 후 데이터베이스에 저장`() {
        // given
        val now = Instant.now()
        val viewAvro1 = ViewAvro(1L, 2L, now)
        val viewAvro2 = ViewAvro(3L, 4L, now)

        val record1 = mockk<ConsumerRecord<String, ViewAvro>> {
            every { value() } returns viewAvro1
        }
        val record2 = mockk<ConsumerRecord<String, ViewAvro>> {
            every { value() } returns viewAvro2
        }

        val expectedViewLogs = listOf(
            ViewLog(workId = 1L, memberId = 2L, viewedAt = now),
            ViewLog(workId = 3L, memberId = 4L, viewedAt = now)
        )

        every { viewLogRepository.bulkInsert(any()) } returns Unit

        // when
        viewLogCommandService.consumeMessage(listOf(record1, record2), acknowledgment)

        // then
        verify {
            viewLogRepository.bulkInsert(match { viewLogs ->
                viewLogs.size == 2 &&
                        viewLogs[0].workId == expectedViewLogs[0].workId &&
                        viewLogs[0].memberId == expectedViewLogs[0].memberId &&
                        viewLogs[1].workId == expectedViewLogs[1].workId &&
                        viewLogs[1].memberId == expectedViewLogs[1].memberId
            })
        }
        verify { acknowledgment.acknowledge() }
    }

    @Test
    fun `작품번호 별 조회이력 삭제`() {
        // given
        val workId = 1L

        // when
        viewLogCommandService.deleteByWorkId(workId)

        // then
        verify { viewLogRepository.deleteByWorkId(workId) }
    }
}
