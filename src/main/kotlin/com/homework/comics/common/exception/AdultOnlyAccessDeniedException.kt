package com.homework.comics.common.exception

class AdultOnlyAccessDeniedException(
    message: String = "성인인증이 필요한 서비스입니다."
) : RuntimeException(message)
