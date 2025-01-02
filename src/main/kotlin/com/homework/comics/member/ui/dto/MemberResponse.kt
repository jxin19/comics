package com.homework.comics.member.ui.dto

import com.homework.comics.member.application.dto.MemberServiceResponse

data class MemberResponse(
    val id: Long,
    val username: String,
    val emailAddress: String,
    val adultVerified: Boolean,
) {
    companion object {
        fun of(memberServiceResponse: MemberServiceResponse) = MemberResponse(
            id = memberServiceResponse.id,
            username = memberServiceResponse.username,
            emailAddress = memberServiceResponse.emailAddress,
            adultVerified = memberServiceResponse.adultVerified
        )
    }
}
