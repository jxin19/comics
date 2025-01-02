package com.homework.comics.member.application.dto

data class AuthServiceResponse(
    val accessToken: String,
    val refreshToken: String
)
