package com.homework.comics.common.config

import com.homework.comics.common.component.MemberAuthenticationProvider
import com.homework.comics.common.handler.NoAccessHandler
import com.homework.comics.common.handler.UnauthenticationHandler
import com.homework.comics.member.application.MemberQueryService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * Spring Security 설정 클래스
 *
 * @property authenticationFilter 커스텀 인증 필터
 * @property memberAuthenticationProvider 인증을 처리하는 인증 제공자
 * @property memberQueryService 를 사용한 사용자 인증 처리 서비스
 * @property passwordConfig 비밀번호 암호화 설정을 제공하는 PasswordConfig
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val authenticationFilter: AuthenticationFilter,
    private val memberAuthenticationProvider: MemberAuthenticationProvider,
    private val memberQueryService: MemberQueryService,
    private val passwordConfig: PasswordConfig,
) {

    /**
     * HTTP 보안 설정을 구성하는 메서드.
     *
     * @param http HttpSecurity 객체로 보안 관련 설정을 정의하는 데 사용됩니다.
     * @return [SecurityFilterChain] 타입의 보안 필터 체인.
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http.csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(HttpMethod.GET, "/member/**").authenticated()
                    .requestMatchers(
                        "/member/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/api-docs/**",
                        "/swagger-resources/**"
                    ).permitAll()
                    .requestMatchers(HttpMethod.GET, "/work/**", "/work-pricing-policy/**", "/purchase-stats/**", "/view-stats/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .authenticationProvider(memberAuthenticationProvider)
            .exceptionHandling { exception ->
                exception
                    .authenticationEntryPoint(UnauthenticationHandler())
                    .accessDeniedHandler(NoAccessHandler())
            }
            .build()

    /**
     * 인증 관리자를 구성하는 메서드
     *
     * @return [AuthenticationManager] 타입의 인증 공급자
     */
    @Bean
    fun authenticationManager(http: HttpSecurity): AuthenticationManager {
        val authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authManagerBuilder.authenticationProvider(memberAuthenticationProvider)
        return authManagerBuilder.build()
    }

}
