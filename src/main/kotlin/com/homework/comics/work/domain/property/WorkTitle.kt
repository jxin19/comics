package com.homework.comics.work.domain.property

import com.homework.comics.common.domain.StringValidator
import jakarta.persistence.Access
import jakarta.persistence.AccessType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
@Access(AccessType.FIELD)
class WorkTitle : StringValidator {
    @Column(name = "title", nullable = false, length = MAX_LENGTH)
    private var _value: String

    constructor(title: String) {
        validateLength(title, "제목을 입력해주세요.")
        validateRule(title, REGEX, "제목은 $MIN_LENGTH~$MAX_LENGTH 이내로 입력해 주세요.")
        this._value = title.trim()
    }

    val value: String
        get() = _value

    companion object {
        private const val MIN_LENGTH: Int = 1
        private const val MAX_LENGTH: Int = 255
        private const val REGEX: String = "^.{$MIN_LENGTH,$MAX_LENGTH}$"
    }
}
