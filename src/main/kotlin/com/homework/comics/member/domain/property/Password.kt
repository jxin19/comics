package com.homework.comics.member.domain.property

import com.homework.comics.common.domain.StringValidator
import jakarta.persistence.Access
import jakarta.persistence.AccessType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import org.springframework.security.crypto.password.PasswordEncoder

@Embeddable
@Access(AccessType.FIELD)
class Password : StringValidator {

    @Column(name = "password", nullable = false)
    var _value: String
        private set

    constructor(password: String?, passwordEncoder: PasswordEncoder) {
        validateLength(password, EMPTY_ERROR_MESSAGE)
        validateRule(password, PASSWORD_RULE_REGEX, RULE_ERROR_MESSAGE)
        this._value = passwordEncoder.encode(password)
    }

    val value: String
        get() = this._value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Password

        if (_value != other._value) return false

        return true
    }

    override fun hashCode(): Int {
        return _value.hashCode()
    }

    override fun toString(): String {
        return "Password(value='$_value')"
    }

    companion object {
        private const val EMPTY_ERROR_MESSAGE: String = "비밀번호를 입력해주세요."
        private const val PASSWORD_RULE_REGEX: String = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$"
        private const val RULE_ERROR_MESSAGE: String = "영문, 숫자, 특수문자를 포함한 8자 이상 비밀번호를 입력해 주세요."
    }

}
