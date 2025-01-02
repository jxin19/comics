package com.homework.comics.work

import com.homework.comics.viewLog.application.ViewLogCommandService
import com.homework.comics.viewStats.application.ViewStatsCommandService
import com.homework.comics.work.application.WorkCommandService
import com.homework.comics.work.application.WorkQueryService
import com.homework.comics.work.application.dto.WorkServiceRequest
import com.homework.comics.work.application.impl.WorkCommandServiceImpl
import com.homework.comics.work.domain.Work
import com.homework.comics.work.domain.property.Author
import com.homework.comics.work.domain.property.Description
import com.homework.comics.work.domain.property.WorkTitle
import com.homework.comics.work.repository.WorkRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WorkCommandServiceTest {

    private lateinit var workRepository: WorkRepository
    private lateinit var workQueryService: WorkQueryService
    private lateinit var viewStatsCommandService: ViewStatsCommandService
    private lateinit var viewLogCommandService: ViewLogCommandService
    private lateinit var workCommandService: WorkCommandService

    @BeforeEach
    fun setup() {
        workRepository = mock(WorkRepository::class.java)
        workQueryService = mock(WorkQueryService::class.java)
        viewStatsCommandService = mock(ViewStatsCommandService::class.java)
        viewLogCommandService = mock(ViewLogCommandService::class.java)
        workCommandService = WorkCommandServiceImpl(
            workRepository,
            workQueryService,
            viewStatsCommandService,
            viewLogCommandService
        )
    }

    private fun createWorkRequest() = WorkServiceRequest(
        title = "작품제목",
        description = "작품설명",
        author = "작가이름",
        isActive = true
    )

    private fun createWork(
        id: Long = 1L,
        title: String = "작품제목",
        description: String = "작품설명",
        author: String = "작가이름",
        isActive: Boolean = true
    ) = Work(
        id = id,
        title = WorkTitle(title),
        description = Description(description),
        author = Author(author),
        _isActive = isActive
    )

    @Test
    fun `작품 생성 성공`() {
        // given
        val request = createWorkRequest()
        val work = createWork()

        `when`(workRepository.save(any(Work::class.java))).thenReturn(work)
        doNothing().`when`(viewStatsCommandService).create(work)

        // when
        val result = workCommandService.create(request)

        // then
        assertEquals(work.id, result.id)
        assertEquals(work.titleValue, result.title)
        verify(workRepository).save(any())
        verify(viewStatsCommandService).create(work)
    }

    @Test
    fun `작품 생성 실패 - 제목 누락`() {
        // given
        val request = WorkServiceRequest(
            title = "",
            description = "작품설명",
            author = "작가이름"
        )

        // when & then
        assertThrows<IllegalArgumentException>("제목을 입력해주세요.") {
            workCommandService.create(request)
        }
    }

    @Test
    fun `작품 생성 실패 - 제목 길이 초과`() {
        // given
        val request = WorkServiceRequest(
            title = "a".repeat(256),
            description = "작품설명",
            author = "작가이름"
        )

        // when & then
        assertThrows<IllegalArgumentException>("제목은 1~255 이내로 입력해 주세요.") {
            workCommandService.create(request)
        }
    }

    @Test
    fun `작품 생성 실패 - 작가명 길이 초과`() {
        // given
        val request = WorkServiceRequest(
            title = "작품제목",
            description = "작품설명",
            author = "a".repeat(101)
        )

        // when & then
        assertThrows<IllegalArgumentException>("작가명은 1~100 이내로 입력해 주세요.") {
            workCommandService.create(request)
        }
    }

    @Test
    fun `작품 수정 성공`() {
        // given
        val id = 1L
        val request = WorkServiceRequest("수정된제목", "수정된설명", "수정된작가")
        val work = createWork()

        `when`(workQueryService.fetchById(id)).thenReturn(work)
        doNothing().`when`(viewStatsCommandService).update(work)

        // when
        val result = workCommandService.update(id, request)

        // then
        assertEquals("수정된제목", result.title)
        verify(workQueryService).fetchById(id)
        verify(viewStatsCommandService).update(work)
    }

    @Test
    fun `작품 삭제 성공`() {
        // given
        val id = 1L
        doNothing().`when`(workRepository).deleteById(id)
        doNothing().`when`(viewLogCommandService).deleteByWorkId(id)

        // when
        workCommandService.delete(id)

        // then
        verify(workRepository).deleteById(id)
        verify(viewLogCommandService).deleteByWorkId(id)
    }

}
