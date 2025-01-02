package com.homework.comics.viewLog.ui

import com.homework.comics.viewLog.application.ViewLogQueryService
import com.homework.comics.viewLog.ui.dto.ViewLogFilterRequest
import com.homework.comics.viewLog.ui.dto.ViewLogResponses
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "조회 이력 API")
@RestController
@RequestMapping("/view-log")
class ViewLogController(
    private val viewLogQueryService: ViewLogQueryService,
) {

    @GetMapping
    @Operation(summary = "작품별 조회 이력")
    fun findByFilter(viewLogFilterRequest: ViewLogFilterRequest): ViewLogResponses {
        val response = viewLogQueryService.findByFilter(viewLogFilterRequest.toServiceDto())
        return ViewLogResponses.of(response)
    }

}
