package com.homework.comics.workPricingPolicy.ui

import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyCommandService
import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyQueryService
import com.homework.comics.workPricingPolicy.ui.dto.WorkPricingPolicyRequest
import com.homework.comics.workPricingPolicy.ui.dto.WorkPricingPolicyResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "작품 가격 정책 API")
@RestController
@RequestMapping("/work-pricing-policy")
class WorkPricingPolicyController(
    private val workPricingPolicyQueryService: WorkPricingPolicyQueryService,
    private val workPricingPolicyCommandService: WorkPricingPolicyCommandService
) {

    @GetMapping("/work/{workId}")
    @Operation(summary = "작품의 현재 적용 가격 정책 조회")
    fun findCurrentPolicy(@PathVariable workId: Long): WorkPricingPolicyResponse? {
        val response = workPricingPolicyQueryService.findCurrentPolicyByWorkId(workId)
        return WorkPricingPolicyResponse.of(response)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "가격 정책 생성")
    fun create(@Valid @RequestBody request: WorkPricingPolicyRequest): WorkPricingPolicyResponse {
        val response = workPricingPolicyCommandService.create(request.toServiceDto())
        return WorkPricingPolicyResponse.of(response)
    }

    @PutMapping("/{id}")
    @Operation(summary = "가격 정책 수정")
    fun update(
        @PathVariable id: Long,
        @Valid @RequestBody request: WorkPricingPolicyRequest
    ): WorkPricingPolicyResponse {
        val response = workPricingPolicyCommandService.update(id, request.toServiceDto())
        return WorkPricingPolicyResponse.of(response)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "가격 정책 삭제")
    fun delete(@PathVariable id: Long) {
        workPricingPolicyCommandService.delete(id)
    }

}
