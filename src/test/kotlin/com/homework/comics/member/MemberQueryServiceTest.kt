package com.homework.comics.member

import com.homework.comics.common.authentication.MemberPrincipal
import com.homework.comics.common.component.AuthenticationFacade
import com.homework.comics.common.component.JwtProvider
import com.homework.comics.common.config.PasswordConfig
import com.homework.comics.member.application.MemberQueryService
import com.homework.comics.member.application.dto.MemberServiceRequest
import com.homework.comics.member.application.impl.MemberQueryServiceImpl
import com.homework.comics.member.repository.MemberRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.BadCredentialsException
import java.util.Optional

@SpringBootTest
class MemberQueryServiceTest {
    private lateinit var memberRepository: MemberRepository
    private lateinit var passwordConfig: PasswordConfig
    private lateinit var jwtProvider: JwtProvider
    private lateinit var memberQueryService: MemberQueryService
    private lateinit var authenticationFacade: AuthenticationFacade

    private val testAccessToken = "test.access.token"
    private val testRefreshToken = "test.refresh.token"
    private val testMemberId = 1L
    private val testEmail = "test@test.com"

    @BeforeEach
    fun setup() {
        memberRepository = mock(MemberRepository::class.java)
        jwtProvider = mock(JwtProvider::class.java)
        authenticationFacade = mock(AuthenticationFacade::class.java)
        passwordConfig = PasswordConfig()
        memberQueryService = MemberQueryServiceImpl(memberRepository, passwordConfig, jwtProvider, authenticationFacade)

        // AuthenticationFacade 기본 설정
        `when`(authenticationFacade.getMemberId()).thenReturn(testMemberId)
        `when`(authenticationFacade.getEmailAddress()).thenReturn(testEmail)
        `when`(authenticationFacade.isAdultVerified()).thenReturn(false)
    }

    @Test
    fun `로그인 성공`() {
        // given
        val request = MemberServiceRequest("testUser", testEmail, "password123!", null)
        val member = request.toEntity(passwordConfig.passwordEncoder())
        val memberPrincipal = MemberPrincipal(member.id, member.emailAddressValue, member.isAdultVerified)

        `when`(memberRepository.findByEmailAddress__value(testEmail)).thenReturn(Optional.of(member))
        `when`(jwtProvider.generateAccessToken(memberPrincipal)).thenReturn(testAccessToken)
        `when`(jwtProvider.generateRefreshToken(memberPrincipal)).thenReturn(testRefreshToken)

        // when
        val result = memberQueryService.login(request)

        // then
        assertNotNull(result)
        assertEquals(testAccessToken, result.accessToken)
        assertEquals(testRefreshToken, result.refreshToken)
        verify(memberRepository).findByEmailAddress__value(testEmail)
    }

    @Test
    fun `로그인 실패 - 존재하지 않는 이메일`() {
        // given
        val request = MemberServiceRequest("testUser", "nonexistent@test.com", "password123!", null)
        `when`(memberRepository.findByEmailAddress__value(anyString())).thenReturn(Optional.empty())

        // when & then
        assertThrows<NoSuchElementException> {
            memberQueryService.login(request)
        }
    }

    @Test
    fun `로그인 실패 - 잘못된 비밀번호`() {
        // given
        val request = MemberServiceRequest("testUser", testEmail, "wrongPassword123!", null)
        val existingMember = MemberServiceRequest("testUser", testEmail, "password123!", null)
            .toEntity(passwordConfig.passwordEncoder())

        `when`(memberRepository.findByEmailAddress__value(testEmail)).thenReturn(Optional.of(existingMember))

        // when & then
        assertThrows<BadCredentialsException> {
            memberQueryService.login(request)
        }
    }

    @Test
    fun `회원 조회 성공`() {
        // given
        val member = MemberServiceRequest("testUser", testEmail, "password123!", null)
            .toEntity(passwordConfig.passwordEncoder())

        `when`(memberRepository.findById(testMemberId)).thenReturn(Optional.of(member))

        // when
        val result = memberQueryService.detail()

        // then
        assertNotNull(result)
        assertEquals(member.emailAddressValue, result.emailAddress)
        verify(authenticationFacade).getMemberId()
    }

    @Test
    fun `회원 조회 실패 - 존재하지 않는 회원`() {
        // given
        `when`(memberRepository.findById(testMemberId)).thenReturn(Optional.empty())

        // when & then
        assertThrows<NoSuchElementException> {
            memberQueryService.detail()
        }
        verify(authenticationFacade).getMemberId()
    }

    @Test
    fun `리프레시 토큰으로 새 액세스 토큰 발급 성공`() {
        // given
        val memberPrincipal = MemberPrincipal(testMemberId, testEmail, false)

        `when`(jwtProvider.validateRefreshToken(testEmail, testRefreshToken)).thenReturn(true)
        `when`(jwtProvider.generateAccessToken(memberPrincipal)).thenReturn("new.access.token")
        `when`(jwtProvider.generateRefreshToken(memberPrincipal)).thenReturn("new.refresh.token")

        // when
        val result = memberQueryService.refreshAccessToken(testRefreshToken)

        // then
        assertNotNull(result)
        assertEquals("new.access.token", result.accessToken)
        assertEquals("new.refresh.token", result.refreshToken)
        verify(authenticationFacade).getMemberId()
        verify(authenticationFacade).getEmailAddress()
        verify(authenticationFacade).isAdultVerified()
    }

    @Test
    fun `리프레시 토큰으로 새 액세스 토큰 발급 실패 - 유효하지 않은 리프레시 토큰`() {
        // given
        `when`(jwtProvider.validateRefreshToken(testEmail, testRefreshToken)).thenReturn(false)

        // when & then
        assertThrows<BadCredentialsException> {
            memberQueryService.refreshAccessToken(testRefreshToken)
        }
        verify(authenticationFacade).getEmailAddress()
    }

    @Test
    fun `로그아웃 성공`() {
        // when
        memberQueryService.logout("Bearer $testAccessToken", testRefreshToken)

        // then
        verify(jwtProvider).invalidateRefreshToken(testAccessToken, testRefreshToken)
    }
}
