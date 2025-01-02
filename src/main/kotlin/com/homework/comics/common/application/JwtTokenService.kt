package com.homework.comics.common.application

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

/**
 * Redis를 사용하여 JWT 리프레시 토큰을 관리하는 서비스
 *
 * @property redisTemplate RedisTemplate 인스턴스
 */
@Service
class JwtTokenService(
    private val redisTemplate: RedisTemplate<String, String>
) {

    private val refreshTokenPrefix = "refreshToken:"

    /**
     * 리프레시 토큰을 Redis에 저장하고 만료 시간을 설정
     *
     * @param emailAddress 리프레시 토큰과 연결된 사용자의 이메일
     * @param refreshToken 저장할 리프레시 토큰
     * @param expirationMinutes 리프레시 토큰의 만료 시간
     */
    fun saveRefreshToken(emailAddress: String, refreshToken: String, expirationMinutes: Long) {
        val key = "$refreshTokenPrefix$emailAddress"
        invalidateRefreshToken(emailAddress)
        redisTemplate.opsForValue().set(key, refreshToken, expirationMinutes, TimeUnit.SECONDS)
    }

    /**
     * 리프레시 토큰이 유효한지 검증
     *
     * @param emailAddress 리프레시 토큰과 연결된 사용자의 이메일
     * @param refreshToken 검증할 리프레시 토큰
     * @return 저장된 토큰과 주어진 토큰이 일치하면 `true`, 그렇지 않으면 `false`
     */
    fun validateRefreshToken(emailAddress: String, refreshToken: String): Boolean {
        val key = "$refreshTokenPrefix$emailAddress"
        val storedToken = redisTemplate.opsForValue().get(key)
        return storedToken == refreshToken
    }

    /**
     * 리프레시 토큰을 무효화합니다
     *
     * @param emailAddress 리프레시 토큰과 연결된 사용자의 이메일
     * @return 토큰이 성공적으로 삭제되면 `true`, 그렇지 않으면 `false`
     */
    fun invalidateRefreshToken(emailAddress: String): Boolean {
        val key = "$refreshTokenPrefix$emailAddress"
        return redisTemplate.delete(key)
    }

}
