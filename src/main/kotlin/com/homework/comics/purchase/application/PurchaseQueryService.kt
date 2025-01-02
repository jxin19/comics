package com.homework.comics.purchase.application

import com.homework.comics.purchase.application.dto.PurchaseServiceResponse

interface PurchaseQueryService {
    fun findById(id: Long): PurchaseServiceResponse
    fun findByMemberId(memberId: Long): List<PurchaseServiceResponse>
    fun findByWorkId(workId: Long): List<PurchaseServiceResponse>
}
