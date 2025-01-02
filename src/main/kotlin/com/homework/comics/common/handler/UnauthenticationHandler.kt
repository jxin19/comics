package com.homework.comics.common.handler

import com.homework.comics.common.handler.util.ExceptionHandlerUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

class UnauthenticationHandler : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?,
    ) {
        ExceptionHandlerUtil.handleException(response!!, BadCredentialsException("로그인이 필요한 기능입니다."))
    }
}
