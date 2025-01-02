package com.homework.comics.work.application

import com.homework.comics.work.application.dto.WorkServiceRequest
import com.homework.comics.work.application.dto.WorkServiceResponse

interface WorkCommandService {
    fun create(request: WorkServiceRequest): WorkServiceResponse
    fun update(id: Long, request: WorkServiceRequest): WorkServiceResponse
    fun delete(id: Long) // 작품 및 이력 삭제 요구사항
}
