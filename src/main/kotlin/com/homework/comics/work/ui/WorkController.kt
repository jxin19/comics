package com.homework.comics.work.ui

import com.homework.comics.common.aop.ViewLog
import com.homework.comics.work.application.WorkCommandService
import com.homework.comics.work.application.WorkQueryService
import com.homework.comics.work.ui.dto.WorkRequest
import com.homework.comics.work.ui.dto.WorkResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "작품 관련 API", description = "작품 정보를 관리하는 API")
@RestController
@RequestMapping("/work")
class WorkController(
    private val workQueryService: WorkQueryService,
    private val workCommandService: WorkCommandService
) {

    @Operation(summary = "작품 상세 조회")
    @GetMapping("/{id}")
    @ViewLog
    fun detail(@PathVariable id: Long): WorkResponse {
        val response = workQueryService.findById(id)
        return WorkResponse.of(response)
    }

    @Operation(summary = "작품 생성")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: WorkRequest): WorkResponse {
        val response = workCommandService.create(request.toServiceDto())
        return WorkResponse.of(response)
    }

    @Operation(summary = "작품 수정")
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: WorkRequest
    ): WorkResponse {
        val response = workCommandService.update(id, request.toServiceDto())
        return WorkResponse.of(response)
    }

    @Operation(summary = "작품 및 조회 이력 삭제")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        workCommandService.delete(id)
    }

}
