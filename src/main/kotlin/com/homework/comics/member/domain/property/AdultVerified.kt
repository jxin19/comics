package com.homework.comics.member.domain.property

import com.homework.comics.common.domain.StringValidator
import jakarta.persistence.Access
import jakarta.persistence.AccessType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Embeddable
@Access(AccessType.FIELD)
class AdultVerified : StringValidator {

    @Column(name = "adult_verified", nullable = false)
    var _value: Boolean = false
        private set

    constructor(identityCheckValue: String?) {
        validateLength(identityCheckValue, EMPTY_ERROR_MESSAGE)
        validateRule(identityCheckValue, ADULT_VERIFIED_REGEX, RULE_ERROR_MESSAGE)
        validateBirthday(identityCheckValue!!)
        this._value = true
    }

    private fun validateBirthday(identityCheckValue: String) {
        val matchResult = ADULT_VERIFIED_REGEX.toRegex()
            .find(identityCheckValue)
            ?: throw IllegalArgumentException(PARSING_ERROR_MESSAGE)
        val (yearPart, month, day, gender) = matchResult.destructured

        val year = when (gender.toInt()) {
            1, 2 -> "19$yearPart".toInt() // 1900년대
            3, 4 -> "20$yearPart".toInt() // 2000년대
            else -> throw IllegalArgumentException(RULE_ERROR_MESSAGE)
        }

        val birthDate = LocalDate.of(year, month.toInt(), day.toInt())
        val age = ChronoUnit.YEARS.between(birthDate, LocalDate.now()) // 만 19세 이상 확인

        if (age < 19) throw IllegalArgumentException(INVALID_MESSAGE)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AdultVerified

        return _value == other._value
    }

    override fun hashCode(): Int {
        return _value.hashCode()
    }

    override fun toString(): String {
        return "AdultVerified(_value=$_value)"
    }

    companion object {
        private const val EMPTY_ERROR_MESSAGE: String = "주민번호를 입력해주세요."
        private const val PARSING_ERROR_MESSAGE: String = "주민등록번호를 파싱할 수 없습니다."
        private const val ADULT_VERIFIED_REGEX: String = "^(\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])-?([1-4])\\d{6}$"
        private const val RULE_ERROR_MESSAGE: String = "올바른 주민번호를 입력해주세요."
        private const val INVALID_MESSAGE: String = "성인만 인증할 수 있습니다."
    }

}
