package com.homework.comics.member.domain.property

import com.homework.comics.common.domain.StringValidator
import jakarta.persistence.Access
import jakarta.persistence.AccessType
import jakarta.persistence.Embeddable
import jakarta.persistence.Column

@Embeddable
@Access(AccessType.FIELD)
class EmailAddress : StringValidator {

    @Column(name = "email", nullable = false, unique = true, length = 120)
    private var _value: String = ""

    constructor()

    constructor(emailAddress: String? = null) {
        validateRule(emailAddress, REGEX, RULE_ERROR_MESSAGE)
        this._value = emailAddress!!
    }

    val value: String
        get() = _value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EmailAddress
        return _value == other._value
    }

    override fun hashCode(): Int = _value.hashCode()

    override fun toString(): String = _value

    companion object {
        private const val REGEX = "[0-9a-zA-Z]([-_.]?[0-9a-zA-Z._\\-])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*[.]([a-zA-Z]{2,})+$"
        private const val RULE_ERROR_MESSAGE = "올바른 이메일주소를 입력해주세요."
    }

}
