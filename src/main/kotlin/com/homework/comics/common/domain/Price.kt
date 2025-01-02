package com.homework.comics.common.domain

import jakarta.persistence.Access
import jakarta.persistence.AccessType
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.math.BigDecimal
import java.math.RoundingMode

@Embeddable
@Access(AccessType.FIELD)
class Price : PriceValidator {
    @Column(name = "price", nullable = false)
    private var _value: BigDecimal

    constructor(price: BigDecimal) {
        validate(price, "가격은 0 이상이어야 합니다.")
        this._value = price.setScale(2, RoundingMode.HALF_UP)
    }

    val value: BigDecimal
        get() = _value
}

