package com.homework.comics.common.config

import com.homework.comics.common.authentication.AuthenticationToken
import com.homework.comics.common.authentication.MemberPrincipal
import com.homework.comics.common.component.JwtProvider
import com.homework.comics.common.handler.util.ExceptionHandlerUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

/**
 * JWT 인증을 처리 필터 클래스
 *
 * @property jwtProvider
 */
@Configuration
class AuthenticationFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {

    /**
     * 요청 필터링 로직을 정의합니다.
     * JWT 토큰을 추출하고 검증한 후, 유효한 토큰일 경우 SecurityContext에 인증 정보를 설정합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param filterChain 다른 필터로 요청을 전달하는 체인 객체
     * @throws ServletException 서블릿 예외가 발생한 경우
     * @throws IOException I/O 예외가 발생한 경우
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            request.getHeader("Authorization")
                ?.removePrefix("Bearer ")
                ?.takeIf { jwtProvider.validateToken(it) }
                ?.let {
                    val memberPrincipal = MemberPrincipal(
                        memberId = jwtProvider.getMemberId(it),
                        emailAddress = jwtProvider.getEmailAddress(it),
                        isAdultVerified = jwtProvider.getAdultVerified(it)
                    )
                    AuthenticationToken(memberPrincipal, ArrayList()).apply {
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                        SecurityContextHolder.getContext().authentication = this
                    }
                }
        } catch (e: Exception) {
            ExceptionHandlerUtil.handleException(response, e)
            return
        }

        filterChain.doFilter(request, response)
    }

}
