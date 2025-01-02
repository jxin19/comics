package com.homework.comics.work

import com.homework.comics.common.component.AuthenticationFacade
import com.homework.comics.work.application.WorkQueryService
import com.homework.comics.work.application.impl.WorkQueryServiceImpl
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
import java.util.Optional

@SpringBootTest
class WorkQueryServiceTest {

    private lateinit var workRepository: WorkRepository
    private lateinit var workQueryService: WorkQueryService
    private lateinit var authenticationFacade: AuthenticationFacade

    @BeforeEach
    fun setup() {
        workRepository = mock(WorkRepository::class.java)
        authenticationFacade = mock(AuthenticationFacade::class.java)
        workQueryService = WorkQueryServiceImpl(workRepository, authenticationFacade)
    }

    @Test
    fun `작품 조회 성공`() {
        // given
        val id = 1L
        val work = Work(
            id = id,
            title = WorkTitle("작품제목"),
            description = Description("작품설명"),
            author = Author("작가이름")
        )

        `when`(workRepository.findById(id)).thenReturn(Optional.of(work))

        // when
        val result = workQueryService.findById(id)

        // then
        assertEquals(work.id, result.id)
        assertEquals(work.titleValue, result.title)
        verify(workRepository).findById(id)
    }

    @Test
    fun `작품 조회 실패 - 존재하지 않는 작품`() {
        // given
        val id = 999L
        `when`(workRepository.findById(id)).thenReturn(Optional.empty())

        // when & then
        assertThrows<NoSuchElementException> {
            workQueryService.findById(id)
        }
    }

}
