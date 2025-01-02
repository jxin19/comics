package com.homework.comics.purchase.ui

import com.homework.comics.purchase.application.PurchaseCommandService
import com.homework.comics.purchase.application.PurchaseQueryService
import com.homework.comics.purchase.ui.dto.PurchaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "구매 관련 API")
@RestController
@RequestMapping("/purchase")
class PurchaseController(
    private val purchaseQueryService: PurchaseQueryService,
    private val purchaseCommandService: PurchaseCommandService
) {

    @GetMapping("/{id}")
    @Operation(summary = "구매 내역 상세 조회")
    fun detail(@PathVariable id: Long): PurchaseResponse {
        return PurchaseResponse.of(purchaseQueryService.findById(id))
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "회원별 구매 내역 조회")
    fun findByMemberId(@PathVariable memberId: Long): List<PurchaseResponse> {
        return purchaseQueryService.findByMemberId(memberId).map(PurchaseResponse::of)
    }

    @GetMapping("/work/{workId}")
    @Operation(summary = "작품별 구매 내역 조회")
    fun findByWorkId(@PathVariable workId: Long): List<PurchaseResponse> {
        return purchaseQueryService.findByWorkId(workId).map(PurchaseResponse::of)
    }

    @PostMapping("/work/{workId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "작품 구매")
    fun purchase(@PathVariable workId: Long): PurchaseResponse {
        return PurchaseResponse.of(purchaseCommandService.purchase(workId))
    }

}
