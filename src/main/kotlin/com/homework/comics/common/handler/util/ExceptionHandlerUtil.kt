package com.homework.comics.common.handler.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.homework.comics.common.dto.BaseResponse
import io.jsonwebtoken.JwtException
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class ExceptionHandlerUtil {
    companion object {
        private val objectMapper = ObjectMapper()

        /**
         * 발생한 예외에 따라 적절한 HTTP 상태 코드와 JSON 형식의 오류 응답을 클라이언트에 반환하는 메서드.
         *
         * @param response HTTP 응답 객체
         * @param e 처리할 예외 객체
         */
        fun handleException(response: HttpServletResponse, e: Exception) {
            val status = when (e) {
                is JwtException -> HttpStatus.UNAUTHORIZED
                is BadCredentialsException -> HttpStatus.UNAUTHORIZED
                is AccessDeniedException -> HttpStatus.FORBIDDEN
                is IOException -> HttpStatus.INTERNAL_SERVER_ERROR
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }

            response.status = status.value()
            response.contentType = "application/json;charset=UTF-8"
            val baseResponse = BaseResponse.of(status, e.message ?: "서버 오류가 발생했습니다.")
            response.writer.write(objectMapper.writeValueAsString(baseResponse.body))
        }
    }
}
