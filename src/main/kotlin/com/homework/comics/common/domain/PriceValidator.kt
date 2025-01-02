package com.homework.comics.common.domain

import java.math.BigDecimal

/**
 * 가격 유효성을 검사하는 인터페이스.
 */
interface PriceValidator {
    /**
     * 가격을 검증하는 메소드.
     * 주어진 가격이 null 아니고 0 이상인지 확인합니다.
     *
     * @param price 검증할 가격
     * @param message 유효하지 않은 가격일 경우 예외로 던질 메시지
     * @throws IllegalArgumentException 가격이 null 또는 0보다 작은 경우 예외 발생
     */
    fun validate(price: BigDecimal?, message: String) {
        require(price != null && price >= BigDecimal.ZERO) { message }
    }
}
