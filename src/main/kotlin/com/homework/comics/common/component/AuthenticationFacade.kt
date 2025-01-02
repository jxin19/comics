package com.homework.comics.common.component

import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Component
class AuthenticationFacade(
    private val jwtProvider: JwtProvider
) {

    fun getMemberId(): Long? =
        getToken()?.let { jwtProvider.getMemberId(it) }

    fun getEmailAddress(): String? =
        getToken()?.let { jwtProvider.getEmailAddress(it) }

    fun isAdultVerified(): Boolean =
        getToken()?.let { jwtProvider.getAdultVerified(it) } ?: false

    private fun getToken(): String? =
        (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes)
            .request
            .getHeader("Authorization")

}
