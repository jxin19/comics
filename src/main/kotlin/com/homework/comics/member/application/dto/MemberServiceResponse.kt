package com.homework.comics.member.application.dto

import com.homework.comics.member.domain.Member

data class MemberServiceResponse(
    val id: Long = 0,
    val username: String = "",
    val emailAddress: String = "",
    val adultVerified: Boolean = false,
) {
    companion object {
        fun of(member: Member) = MemberServiceResponse(
            id = member.id,
            emailAddress = member.emailAddressValue,
            username = member.usernameValue,
            adultVerified = member.isAdultVerified
        )
    }
}
