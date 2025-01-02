package com.homework.comics.work.domain.property

import com.homework.comics.common.domain.StringValidator
import jakarta.persistence.Access
import jakarta.persistence.AccessType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
@Access(AccessType.FIELD)
class Author : StringValidator {
    @Column(name = "author", nullable = false, length = MAX_LENGTH)
    private var _value: String

    constructor(value: String) {
        validateLength(value, "작가명을 입력해주세요.")
        validateRule(value, REGEX, "작가명은 $MIN_LENGTH~$MAX_LENGTH 이내로 입력해 주세요.")
        this._value = value.trim()
    }

    val value: String
        get() = _value

    companion object {
        private const val MIN_LENGTH: Int = 1
        private const val MAX_LENGTH: Int = 100
        private const val REGEX: String = "^.{$MIN_LENGTH,$MAX_LENGTH}$"
    }
}
