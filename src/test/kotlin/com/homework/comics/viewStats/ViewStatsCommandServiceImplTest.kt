package com.homework.comics.viewStats

import com.homework.comics.viewLog.application.ViewLogQueryService
import com.homework.comics.viewStats.application.ViewStatsQueryService
import com.homework.comics.viewStats.application.impl.ViewStatsCommandServiceImpl
import com.homework.comics.viewStats.domain.ViewStats
import com.homework.comics.viewStats.repository.ViewStatsRepository
import com.homework.comics.work.domain.Work
import com.homework.comics.work.domain.property.Author
import com.homework.comics.work.domain.property.Description
import com.homework.comics.work.domain.property.WorkTitle
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations

class ViewStatsCommandServiceImplTest {
    private lateinit var viewStatsRepository: ViewStatsRepository
    private lateinit var viewStatsQueryService: ViewStatsQueryService
    private lateinit var viewLogQueryService: ViewLogQueryService
    private lateinit var redisTemplate: RedisTemplate<String, String>
    private lateinit var valueOperations: ValueOperations<String, String>
    private lateinit var viewStatsCommandService: ViewStatsCommandServiceImpl

    @BeforeEach
    fun setUp() {
        viewStatsRepository = mockk(relaxed = true)
        viewStatsQueryService = mockk(relaxed = true)
        viewLogQueryService = mockk(relaxed = true)
        redisTemplate = mockk(relaxed = true)
        valueOperations = mockk(relaxed = true)
        every { redisTemplate.opsForValue() } returns valueOperations
        viewStatsCommandService = ViewStatsCommandServiceImpl(
            viewStatsRepository,
            viewStatsQueryService,
            viewLogQueryService,
            redisTemplate
        )
    }

    @Test
    fun `작품 생성 시 조회 통계 데이터도 함께 생성`() {
        // given
        val work = createWork(1L)
        val slot = slot<ViewStats>()
        every { viewStatsRepository.save(capture(slot)) } returns mockk()

        // when
        viewStatsCommandService.create(work)

        // then
        verify { viewStatsRepository.save(any()) }
        assertEquals(work.id, slot.captured.workId)
        assertEquals(work.titleValue, slot.captured.work.title)
        assertEquals(work.authorValue, slot.captured.work.author)
    }

    @Test
    fun `작품 정보 업데이트 시 조회 통계의 작품 정보도 함께 업데이트`() {
        // given
        val work = createWork(1L)
        val existingViewStats = mockk<ViewStats>(relaxed = true)
        every { viewStatsQueryService.fetchByWorkId(1L) } returns existingViewStats
        every { viewStatsRepository.save(any()) } returns mockk()

        // when
        viewStatsCommandService.update(work)

        // then
        verify {
            existingViewStats.updateWorkInfo(match {
                it.id == work.id &&
                        it.title == work.titleValue &&
                        it.author == work.authorValue
            })
            viewStatsRepository.save(existingViewStats)
        }
    }

    private fun createWork(id: Long) = Work(
        id = id,
        title = WorkTitle("테스트 작품 $id"),
        description = Description("테스트 설명 $id"),
        author = Author("테스트 작가")
    )
}
