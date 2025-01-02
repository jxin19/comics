package com.homework.comics.common.handler

import com.homework.comics.common.dto.BaseResponse
import com.homework.comics.common.exception.AdultOnlyAccessDeniedException
import jakarta.persistence.EntityNotFoundException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(ex: HttpMessageNotReadableException) =
        BaseResponse.of(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<BaseResponse> {
        val errors: Map<String, String?> = ex.bindingResult.allErrors
            .filterIsInstance<FieldError>()
            .associate { error -> error.field to error.defaultMessage }
        return BaseResponse.of(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.", errors)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException) =
        BaseResponse.of(HttpStatus.BAD_REQUEST, ex.message ?: "잘못된 요청입니다.")

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(ex: NoSuchElementException) =
        BaseResponse.of(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(ex: EntityNotFoundException) =
        BaseResponse.of(HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(ex: DataIntegrityViolationException) =
        BaseResponse.of(HttpStatus.CONFLICT, ex.message)

    @ExceptionHandler(AdultOnlyAccessDeniedException::class)
    fun handleAdultOnlyAccessDeniedException(e: AdultOnlyAccessDeniedException) =
        BaseResponse.of(HttpStatus.CONFLICT, e.message)

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception) =
        BaseResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, ex.message)
}

