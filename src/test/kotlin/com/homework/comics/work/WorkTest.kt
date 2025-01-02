package com.homework.comics.work

import com.homework.comics.work.domain.Work
import com.homework.comics.work.domain.property.Author
import com.homework.comics.work.domain.property.Description
import com.homework.comics.work.domain.property.WorkTitle
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class WorkTest {

    private fun createValidWork(
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
        val work = createValidWork()

        assertEquals("작품제목", work.titleValue)
        assertEquals("작품설명", work.descriptionValue)
        assertEquals("작가이름", work.authorValue)
        assertTrue(work.isActive)
    }

    @Test
    fun `작품 수정 성공`() {
        val work = createValidWork()
        val updatedWork = createValidWork(
            title = "수정제목",
            description = "수정설명",
            author = "수정작가",
            isActive = false
        )

        work.update(updatedWork)

        assertEquals("수정제목", work.titleValue)
        assertEquals("수정설명", work.descriptionValue)
        assertEquals("수정작가", work.authorValue)
        assertFalse(work.isActive)
    }

    @Test
    fun `작품 생성 실패 - 제목 유효성 검증`() {
        assertThrows<IllegalArgumentException>("제목을 입력해주세요.") {
            createValidWork(title = "")
        }
        assertThrows<IllegalArgumentException>("제목은 1~255 이내로 입력해 주세요.") {
            createValidWork(title = "a".repeat(256))
        }
    }

    @Test
    fun `작품 생성 실패 - 작가명 유효성 검증`() {
        assertThrows<IllegalArgumentException>("작가명을 입력해주세요.") {
            createValidWork(author = "")
        }
        assertThrows<IllegalArgumentException>("작가명은 1~100 이내로 입력해 주세요.") {
            createValidWork(author = "a".repeat(101))
        }
    }

}
