package com.homework.comics.purchase.application

import com.homework.comics.purchase.application.dto.PurchaseServiceResponse

interface PurchaseCommandService {
    fun purchase(workId: Long): PurchaseServiceResponse
}
