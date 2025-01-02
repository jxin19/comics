package com.homework.comics.common.dto

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class BaseResponse(
    val meta: Meta,
    val data: Any? = null,
) {
    companion object {
        fun of(status: HttpStatus, data: Any? = null) : ResponseEntity<BaseResponse> = of(status, null, data)
        fun of(status: HttpStatus, message: String? = null, data: Any? = null) : ResponseEntity<BaseResponse> {
            val response = BaseResponse(
                meta = Meta(
                    code = status.value(),
                    message = message ?: status.reasonPhrase.lowercase()
                ),
                data = data
            )
            return ResponseEntity.status(status)
                .body(response)
        }
    }
}
