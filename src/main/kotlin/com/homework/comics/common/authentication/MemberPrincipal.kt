package com.homework.comics.common.authentication

data class MemberPrincipal(
    val memberId: Long,
    val emailAddress: String,
    val isAdultVerified: Boolean
)
