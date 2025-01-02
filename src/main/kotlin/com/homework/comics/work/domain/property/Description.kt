package com.homework.comics.work.domain.property

import com.homework.comics.common.domain.StringValidator
import jakarta.persistence.*

@Embeddable
@Access(AccessType.FIELD)
class Description : StringValidator {
    @Column(name = "description", columnDefinition = "TEXT")
    private var _value: String

    constructor(value: String) {
        this._value = value
    }

    val value: String
        get() = this._value
}
