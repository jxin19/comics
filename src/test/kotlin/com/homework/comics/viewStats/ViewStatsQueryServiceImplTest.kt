package com.homework.comics.viewStats

import com.homework.comics.viewStats.application.impl.ViewStatsQueryServiceImpl
import com.homework.comics.viewStats.domain.ViewStats
import com.homework.comics.viewStats.repository.ViewStatsRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import java.time.Instant

class ViewStatsQueryServiceImplTest {
    private lateinit var viewStatsRepository: ViewStatsRepository
    private lateinit var viewStatsQueryService: ViewStatsQueryServiceImpl

    @BeforeEach
    fun setUp() {
        viewStatsRepository = mockk(relaxed = true)
        viewStatsQueryService = ViewStatsQueryServiceImpl(viewStatsRepository)
    }

    @Test
    fun `조회수 기준으로 정렬된 상위 작품 목록을 반환한다`() {
        // given
        val limit = 2
        val workInfo1 = ViewStats.WorkInfo(
            id = 1L,
            title = "테스트 작품 1",
            description = "테스트 설명 1",
            author = "테스트 작가",
            isActive = true,
            isAdultOnly = false
        )
        val workInfo2 = ViewStats.WorkInfo(
            id = 2L,
            title = "테스트 작품 2",
            description = "테스트 설명 2",
            author = "테스트 작가",
            isActive = true,
            isAdultOnly = false
        )

        val viewStats1 = ViewStats(
            id = "test-id-1",
            workId = 1L,
            workInfo = workInfo1,
            totalViews = 2000L,
            lastUpdatedAt = Instant.now()
        )
        val viewStats2 = ViewStats(
            id = "test-id-2",
            workId = 2L,
            workInfo = workInfo2,
            totalViews = 1000L,
            lastUpdatedAt = Instant.now()
        )

        every {
            viewStatsRepository.findByOrderByTotalViewsDesc(PageRequest.of(0, limit))
        } returns listOf(viewStats1, viewStats2)

        // when
        val result = viewStatsQueryService.findTopViewedWorks(limit)

        // then
        assertEquals(2, result.list.size)
        assertEquals(viewStats1.workId, result.list[0].workServiceResponse.id)
        assertEquals(viewStats1.totalViewCount, result.list[0].totalViews)
        assertEquals(viewStats2.workId, result.list[1].workServiceResponse.id)
        assertEquals(viewStats2.totalViewCount, result.list[1].totalViews)
    }
}
