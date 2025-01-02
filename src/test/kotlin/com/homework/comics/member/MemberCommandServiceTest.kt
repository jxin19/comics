package com.homework.comics.member

import com.homework.comics.common.component.AuthenticationFacade
import com.homework.comics.common.config.PasswordConfig
import com.homework.comics.member.application.MemberCommandService
import com.homework.comics.member.application.MemberQueryService
import com.homework.comics.member.application.dto.MemberServiceRequest
import com.homework.comics.member.application.impl.MemberCommandServiceImpl
import com.homework.comics.member.domain.Member
import com.homework.comics.member.repository.MemberRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberCommandServiceTest {
    private lateinit var memberRepository: MemberRepository
    private lateinit var memberQueryService: MemberQueryService
    private lateinit var passwordConfig: PasswordConfig
    private lateinit var memberCommandService: MemberCommandService
    private lateinit var authenticationFacade: AuthenticationFacade

    private val testMemberId = 1L

    @BeforeEach
    fun setup() {
        memberRepository = mock(MemberRepository::class.java)
        memberQueryService = mock(MemberQueryService::class.java)
        authenticationFacade = mock(AuthenticationFacade::class.java)
        passwordConfig = PasswordConfig()
        memberCommandService = MemberCommandServiceImpl(
            memberRepository,
            memberQueryService,
            passwordConfig,
            authenticationFacade
        )

        // AuthenticationFacade 기본 설정
        `when`(authenticationFacade.getMemberId()).thenReturn(testMemberId)
    }

    @Test
    fun `회원 생성 성공`() {
        // given
        val request = MemberServiceRequest("testUser", "test@test.com", "password123!", null)
        val member = request.toEntity(passwordConfig.passwordEncoder())

        `when`(memberQueryService.validateDuplicate(
            id = anyLong(),
            username = anyString(),
            emailAddress = anyString()
        )).then { }
        `when`(memberRepository.save(any(Member::class.java))).thenReturn(member)

        // when
        val result = memberCommandService.create(request)

        // then
        assertNotNull(result)
        assertEquals(request.username, result.username)
        verify(memberRepository).save(any(Member::class.java))
    }

    @Test
    fun `회원 수정 성공`() {
        // given
        val request = MemberServiceRequest("newUser", "test@test.com", "password123!", null)
        val existingMember = request.toEntity(passwordConfig.passwordEncoder())

        `when`(memberQueryService.fetchById(testMemberId)).thenReturn(existingMember)
        `when`(memberQueryService.validateDuplicate(
            id = eq(testMemberId),
            username = anyString(),
            emailAddress = anyString()
        )).then { }

        // when
        val result = memberCommandService.update(request)

        // then
        assertNotNull(result)
        assertEquals("newUser", result.username)
        verify(memberQueryService).fetchById(testMemberId)
        verify(authenticationFacade).getMemberId()
    }

    @Test
    fun `성인인증 성공`() {
        // given
        val member = MemberServiceRequest("testUser", "test@test.com", "password123!", null)
            .toEntity(passwordConfig.passwordEncoder())
        val identityCheckValue = "990101-1234567"

        `when`(memberQueryService.fetchById(testMemberId)).thenReturn(member)

        // when
        val result = memberCommandService.verifyAdult(identityCheckValue)

        // then
        assertNotNull(result)
        assertTrue(result.adultVerified)
        verify(memberQueryService).fetchById(testMemberId)
        verify(authenticationFacade).getMemberId()
    }

    @Test
    fun `성인인증 실패 - 잘못된 주민번호`() {
        // given
        val member = MemberServiceRequest("testUser", "test@test.com", "password123!", null)
            .toEntity(passwordConfig.passwordEncoder())
        val invalidIdentityCheckValue = "invalid-value"

        `when`(memberQueryService.fetchById(testMemberId)).thenReturn(member)

        // when & then
        assertThrows<IllegalArgumentException> {
            memberCommandService.verifyAdult(invalidIdentityCheckValue)
        }
        verify(memberQueryService).fetchById(testMemberId)
        verify(authenticationFacade).getMemberId()
    }

    @Test
    fun `성인인증 실패 - 미성년자`() {
        // given
        val member = MemberServiceRequest("testUser", "test@test.com", "password123!", null)
            .toEntity(passwordConfig.passwordEncoder())
        val underageIdentityCheckValue = "080101-3234567"  // 2008년생

        `when`(memberQueryService.fetchById(testMemberId)).thenReturn(member)

        // when & then
        assertThrows<IllegalArgumentException> {
            memberCommandService.verifyAdult(underageIdentityCheckValue)
        }
        verify(memberQueryService).fetchById(testMemberId)
        verify(authenticationFacade).getMemberId()
    }
}
