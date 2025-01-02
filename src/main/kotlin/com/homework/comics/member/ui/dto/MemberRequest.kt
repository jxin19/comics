package com.homework.comics.member.ui.dto

import com.homework.comics.member.application.dto.MemberServiceRequest
import jakarta.validation.constraints.NotBlank

data class MemberRequest(
    @field:NotBlank(message = "회원네임을 입력해주세요.")
    val username: String,

    @field:NotBlank(message = "이메일주소를 입력해주세요.")
    val emailAddress: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String,

    val identityCheckValue: String?
) {
    fun toServiceDto() = MemberServiceRequest(
        username = username,
        emailAddress = emailAddress,
        password = password,
        identityCheckValue = identityCheckValue
    )
}
