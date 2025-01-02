package com.homework.comics.member.ui.dto

import com.homework.comics.member.application.dto.AuthServiceResponse

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun of(authServiceResponse: AuthServiceResponse) : AuthResponse = AuthResponse(
            accessToken = authServiceResponse.accessToken,
            refreshToken = authServiceResponse.refreshToken
        )
    }
}
