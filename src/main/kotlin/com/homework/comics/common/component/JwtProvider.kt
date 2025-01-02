package com.homework.comics.common.component

import com.homework.comics.common.application.JwtTokenService
import com.homework.comics.common.authentication.MemberPrincipal
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

/**
 * JWT 토큰을 생성하고 검증하는 기능을 제공하는 클래스.
 */
@Component
class JwtProvider(private val jwtTokenService: JwtTokenService) {

    @Value("\${jwt.access-token-expire-time}")
    private var ACCESS_TOKEN_EXPIRE_TIME: Long = 0

    @Value("\${jwt.refresh-token-expire-time}")
    private var REFRESH_TOKEN_EXPIRE_TIME: Long = 0

    @Value("\${jwt.secret-key}")
    private lateinit var SECRET_KEY: String

    val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))
    }

    /**
     * 액세스 토큰을 생성합니다.
     *
     * @param memberId 토큰에 포함할 회원번호
     * @param emailAddress 토큰에 포함할 사용자 이메일
     * @param isAdultVerified 토큰에 포함할 성인인증 여부
     * @return 생성된 액세스 토큰
     */
    fun generateAccessToken(memberPrincipal: MemberPrincipal): String =
        generateToken(memberPrincipal, ACCESS_TOKEN_EXPIRE_TIME)

    /**
     * 리프레시 토큰을 생성합니다.
     *
     * @param memberId 토큰에 포함할 회원번호
     * @param emailAddress 토큰에 포함할 사용자 이메일
     * @param isAdultVerified 토큰에 포함할 성인인증 여부
     * @return 생성된 리프레시 토큰
     */
    fun generateRefreshToken(memberPrincipal: MemberPrincipal): String =
        generateToken(memberPrincipal, REFRESH_TOKEN_EXPIRE_TIME).also {
            jwtTokenService.saveRefreshToken(memberPrincipal.emailAddress, it, REFRESH_TOKEN_EXPIRE_TIME)
        }

    /**
     * 주어진 이메일와 만료 시간을 기반으로 JWT 토큰을 생성합니다.
     *
     * @param memberId 토큰에 포함할 회원번호
     * @param emailAddress 토큰에 포함할 사용자 이메일
     * @param isAdultVerified 토큰에 포함할 성인인증 여부
     * @param expirationTime 토큰 만료 시간
     * @return 생성된 JWT 토큰
     */
    private fun generateToken(memberPrincipal: MemberPrincipal, expirationTime: Long): String =
        Jwts.builder()
            .claim("memberId", memberPrincipal.memberId)
            .claim("emailAddress", memberPrincipal.emailAddress)
            .claim("isAdultVerified", memberPrincipal.isAdultVerified)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + (expirationTime * 1_000)))
            .signWith(key)
            .compact()

    /**
     * 리프레시 토큰이 유효한지 검증하는 메서드.
     *
     * @param emailAddress 토큰에 연결된 사용자 이메일.
     * @param refreshToken 검증할 리프레시 토큰.
     * @return 토큰이 유효하면 `true`, 그렇지 않으면 `false`.
     */
    fun validateRefreshToken(emailAddress: String, refreshToken: String): Boolean =
        validateToken(refreshToken) && jwtTokenService.validateRefreshToken(emailAddress, refreshToken)

    /**
     * 리프레시 토큰을 무효화하는 메소드.
     *
     * @param accessToken 검증할 액세스 토큰
     * @param refreshToken 검증할 리프레시 토큰
     * @return 토큰이 무효화되었으면 `true`, 그렇지 않으면 `false`
     */
    fun invalidateRefreshToken(accessToken: String, refreshToken: String): Boolean =
        validateToken(refreshToken) && jwtTokenService.invalidateRefreshToken(getEmailAddress(accessToken))

    /**
     * JWT 토큰이 유효한지 검증합니다.
     *
     * @param token 검증할 JWT 토큰
     * @return 토큰이 유효하면 `true`, 그렇지 않으면 `false`
     */
    fun validateToken(token: String): Boolean = getClaims(token).expiration.after(Date())

    /**
     * JWT 토큰에서 회원번호를 추출합니다.
     *
     * @param token 추출할 JWT 토큰
     * @return 토큰에서 추출된 회원번호
     */
    fun getMemberId(token: String): Long = getClaims(token)["memberId"].toString().toLong()

    /**
     * JWT 토큰에서 사용자 이메일를 추출합니다.
     *
     * @param token 추출할 JWT 토큰
     * @return 토큰에서 추출된 이메일 문자열
     */
    fun getEmailAddress(token: String): String = getClaims(token)["emailAddress"] as String

    /**
     * JWT 토큰에서 성인인증 여부를 추출합니다.
     *
     * @param token 추출할 JWT 토큰
     * @return 토큰에서 추출된 성인인증 여부
     */
    fun getAdultVerified(token: String): Boolean = getClaims(token)["isAdultVerified"] as Boolean

    /**
     * JWT 토큰이 만료되었는지 확인합니다.
     *
     * @param token 확인할 JWT 토큰
     * @return 토큰이 만료되었으면 `true`, 그렇지 않으면 `false`
     */
    fun isExpired(token: String): Boolean = getClaims(token).expiration.before(Date())

    /**
     * JWT 토큰에서 [Claims] 객체를 추출합니다.
     *
     * @param token 추출할 JWT 토큰
     * @return 토큰에서 추출된 [Claims] 객체
     */
    fun getClaims(token: String): Claims = runCatching {
        Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token.removePrefix("Bearer "))
            .payload
    }.getOrElse { e ->
        when (e) {
            is ExpiredJwtException -> throw JwtException("로그아웃되었으니, 다시 로그인하십시오.")
            is MalformedJwtException -> throw BadCredentialsException("잘못된 호출입니다. 다시 로그인하십시오.")
            else -> throw e
        }
    }

}
