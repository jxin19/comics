package com.homework.comics.member.ui

import com.homework.comics.member.application.MemberCommandService
import com.homework.comics.member.application.MemberQueryService
import com.homework.comics.member.ui.dto.AuthResponse
import com.homework.comics.member.ui.dto.MemberRequest
import com.homework.comics.member.ui.dto.MemberResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@Tag(name = "회원 관련 API", description = "회원 정보를 관리하는 API")
@RestController
@RequestMapping("/member")
class MemberController(
    private val memberQueryService: MemberQueryService,
    private val memberCommandService: MemberCommandService
) {

    @Operation(summary = "회원 상세 조회", description = "회원의 상세 정보를 조회합니다.")
    @GetMapping
    fun detail(): MemberResponse {
        val response = memberQueryService.detail()
        return MemberResponse.of(response)
    }

    @Operation(summary = "회원 생성", description = "새로운 회원을 생성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: MemberRequest): MemberResponse {
        val response = memberCommandService.create(request.toServiceDto())
        return MemberResponse.of(response)
    }

    @Operation(summary = "회원 수정", description = "기존 회원 정보를 수정합니다.")
    @PutMapping
    fun update(@Valid @RequestBody request: MemberRequest): MemberResponse {
        val response = memberCommandService.update(request.toServiceDto())
        return MemberResponse.of(response)
    }

    @Operation(summary = "성인인증", description = "로그인 토큰과 신분증 번호로 성인인증을 합니다.")
    @PutMapping("/verify-adult/{identityCheckValue}")
    fun verifyAdult(@PathVariable identityCheckValue: String) {
        memberCommandService.verifyAdult(identityCheckValue)
    }

    @Operation(summary = "로그인", description = "로그인 처리 및 토큰 발급합니다.")
    @PostMapping("/login")
    fun login(@RequestBody request: MemberRequest): AuthResponse {
        val authServiceResponse = memberQueryService.login(request.toServiceDto())
        return AuthResponse.of(authServiceResponse)
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰으로 새로운 액세스 토큰 발급합니다.")
    @PostMapping("/refresh-token")
    fun refreshToken(
        @RequestHeader("Refresh-Token") refreshToken: String,
    ): AuthResponse {
        val authServiceResponse = memberQueryService.refreshAccessToken(refreshToken)
        return AuthResponse.of(authServiceResponse)
    }

    @Operation(summary = "로그아웃", description = "로그아웃 처리 및 토큰 무효화합니다.")
    @PostMapping("/logout")
    fun logout(
        @RequestHeader("Authorization") accessToken: String,
        @RequestHeader("Refresh-Token") refreshToken: String,
    ) {
        memberQueryService.logout(accessToken, refreshToken)
    }

}
