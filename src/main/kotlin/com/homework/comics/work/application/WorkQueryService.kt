package com.homework.comics.work.application

import com.homework.comics.work.application.dto.WorkServiceResponse
import com.homework.comics.work.domain.Work

interface WorkQueryService {
    fun fetchAll(): Iterable<Work>
    fun findById(id: Long): WorkServiceResponse
    fun fetchById(id: Long): Work
}
