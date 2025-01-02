package com.homework.comics.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * 비밀번호 인코딩을 위한 설정 클래스.
 */
@Configuration
class PasswordConfig {

    /**
     * 비밀번호를 암호화하기 위한 [PasswordEncoder] 빈을 생성합니다.
     *
     * @return [BCryptPasswordEncoder] 인스턴스
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
