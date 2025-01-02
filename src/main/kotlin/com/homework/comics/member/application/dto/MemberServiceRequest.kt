package com.homework.comics.member.application.dto

import com.homework.comics.member.domain.Member
import com.homework.comics.member.domain.property.AdultVerified
import com.homework.comics.member.domain.property.EmailAddress
import com.homework.comics.member.domain.property.Password
import com.homework.comics.member.domain.property.Username
import org.springframework.security.crypto.password.PasswordEncoder

data class MemberServiceRequest(
    val username: String,
    val emailAddress: String,
    val password: String,
    val identityCheckValue: String?
) {
    fun toEntity(passwordEncoder: PasswordEncoder) = Member(
        username = Username(username),
        emailAddress = EmailAddress(emailAddress),
        password = Password(password, passwordEncoder),
        adultVerified = identityCheckValue?.let { AdultVerified(it) }
    )
}
