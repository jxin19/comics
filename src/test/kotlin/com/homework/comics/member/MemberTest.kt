package com.homework.comics.member

import com.homework.comics.member.domain.Member
import com.homework.comics.member.domain.property.EmailAddress
import com.homework.comics.member.domain.property.Password
import com.homework.comics.member.domain.property.Username
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootTest
class MemberTest {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `회원 생성 테스트`() {
        // given
        val id: Long = 1
        val username = Username("testUser")
        val email = EmailAddress("test@test.com")
        val password = Password("password123!", passwordEncoder)

        // when
        val member = Member(
            id = id,
            username = username,
            emailAddress = email,
            password = password
        )

        // then
        assertEquals(id, member.id)
        assertEquals(username.value, member.usernameValue)
        assertEquals(email.value, member.emailAddressValue)
        assertEquals(password.value, member.passwordValue)
    }

    @Test
    fun `회원 생성 - Username이 빈 값이면 실패`() {
        assertThrows<IllegalArgumentException> {
            Member(
                username = Username(""),
                emailAddress = EmailAddress("test@test.com"),
                password = Password("password123!", passwordEncoder)
            )
        }
    }

    @Test
    fun `회원 생성 - 이메일 형식이 아니면 실패`() {
        assertThrows<IllegalArgumentException> {
            Member(
                username = Username("testUser"),
                emailAddress = EmailAddress("invalid-email"),
                password = Password("password123!", passwordEncoder)
            )
        }
    }

    @Test
    fun `회원 생성 - 비밀번호가 조건에 맞지 않으면 실패`() {
        assertThrows<IllegalArgumentException> {
            Member(
                username = Username("testUser"),
                emailAddress = EmailAddress("test@test.com"),
                password = Password("weak", passwordEncoder)
            )
        }
    }

    @Test
    fun `회원 수정 테스트`() {
        // given
        val member = Member(
            id = 1,
            username = Username("oldUser"),
            emailAddress = EmailAddress("old@test.com"),
            password = Password("oldpass123!", passwordEncoder)
        )

        val updatedMember = Member(
            id = 1,
            username = Username("newUser"),
            emailAddress = EmailAddress("new@test.com"),
            password = Password("newpass123!", passwordEncoder)
        )

        // when
        member.update(updatedMember)

        // then
        assertEquals(updatedMember.usernameValue, member.usernameValue)
        assertEquals(updatedMember.emailAddressValue, member.emailAddressValue)
        assertEquals(updatedMember.passwordValue, member.passwordValue)
    }

    @Test
    fun `회원 업데이트 - 유효하지 않은 데이터로 수정 시도시 실패`() {
        val member = Member(
            username = Username("testUser"),
            emailAddress = EmailAddress("test@test.com"),
            password = Password("password123!", passwordEncoder)
        )

        assertThrows<IllegalArgumentException> {
            member.update(Member(
                username = Username(""),
                emailAddress = EmailAddress("test@test.com"),
                password = Password("password123!", passwordEncoder)
            ))
        }
    }

    @Test
    fun `성인인증 성공 테스트`() {
        // given
        val member = Member(
            username = Username("testUser"),
            emailAddress = EmailAddress("test@test.com"),
            password = Password("password123!", passwordEncoder)
        )
        val validIdentityNumber = "990101-1234567"

        // when
        member.verifyAdult(validIdentityNumber)

        // then
        assertEquals(true, member.isAdultVerified)
    }

    @Test
    fun `성인인증 실패 - 미성년자`() {
        // given
        val member = Member(
            username = Username("testUser"),
            emailAddress = EmailAddress("test@test.com"),
            password = Password("password123!", passwordEncoder)
        )
        val underageIdentityNumber = "080101-3234567"  // 2008년생 (미성년자)

        // when & then
        assertThrows<IllegalArgumentException> {
            member.verifyAdult(underageIdentityNumber)
        }
        assertEquals(false, member.isAdultVerified)
    }

    @Test
    fun `성인인증 실패 - 잘못된 주민번호 형식`() {
        // given
        val member = Member(
            username = Username("testUser"),
            emailAddress = EmailAddress("test@test.com"),
            password = Password("password123!", passwordEncoder)
        )
        val invalidIdentityNumber = "123456-789"

        // when & then
        assertThrows<IllegalArgumentException> {
            member.verifyAdult(invalidIdentityNumber)
        }
        assertEquals(false, member.isAdultVerified)
    }

    @Test
    fun `성인인증 실패 - 빈 주민번호`() {
        // given
        val member = Member(
            username = Username("testUser"),
            emailAddress = EmailAddress("test@test.com"),
            password = Password("password123!", passwordEncoder)
        )
        val emptyIdentityNumber = ""

        // when & then
        assertThrows<IllegalArgumentException> {
            member.verifyAdult(emptyIdentityNumber)
        }
        assertEquals(false, member.isAdultVerified)
    }

}
