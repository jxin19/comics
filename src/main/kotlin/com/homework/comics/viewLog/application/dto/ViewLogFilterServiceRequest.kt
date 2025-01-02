package com.homework.comics.viewLog.application.dto

import java.time.Instant

data class ViewLogFilterServiceRequest(
    val workId: Long = 0,
    val memberId: Int? = null,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val page: Int = 1,
    val size: Int = 20
)
