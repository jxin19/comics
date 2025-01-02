package com.homework.comics.common.component

import com.homework.comics.member.application.MemberQueryService
import com.homework.comics.common.authentication.AuthenticationToken
import com.homework.comics.common.authentication.MemberPrincipal
import com.homework.comics.common.dto.AuthDetails
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

/**
 * 인증을 처리하는 AuthenticationProvider
 *
 * @property memberQueryService
 */
@Component
class MemberAuthenticationProvider(
    private val memberQueryService: MemberQueryService,
) : AuthenticationProvider {

    /**
     * 인증 객체를 사용하여 인증을 수행
     *
     * @param authentication 인증을 위한 정보를 포함하는 Authentication 객체
     * @return 인증된 사용자 정보와 권한을 포함하는 MemberAuthenticationProvider
     * @throws org.springframework.security.core.AuthenticationException 인증 실패 시 발생
     */
    override fun authenticate(authentication: Authentication): Authentication {
        val memberPrincipal = authentication.principal as MemberPrincipal
        val authDetails: AuthDetails = memberQueryService.loadUserByUsername(memberPrincipal.emailAddress)
        return AuthenticationToken(
            MemberPrincipal(
                authDetails.memberId,
                authDetails.emailAddress,
                authDetails.isAdultVerified
            ),
            authDetails.authorities
        )
    }

    /**
     * 이 AuthenticationProvider가 지원하는 인증 타입인지 확인
     *
     * @param authentication 확인할 인증 클래스
     * @return 지원하는 인증 타입이면 true, 그렇지 않으면 false
     */
    override fun supports(authentication: Class<*>): Boolean =
        AuthenticationToken::class.java.isAssignableFrom(authentication)
}
