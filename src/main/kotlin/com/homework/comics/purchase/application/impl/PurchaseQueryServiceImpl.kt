package com.homework.comics.purchase.application.impl

import com.homework.comics.purchase.application.PurchaseQueryService
import com.homework.comics.purchase.application.dto.PurchaseServiceResponse
import com.homework.comics.purchase.repository.PurchaseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PurchaseQueryServiceImpl(
    private val purchaseRepository: PurchaseRepository
) : PurchaseQueryService {

    override fun findById(id: Long): PurchaseServiceResponse {
        val purchase = purchaseRepository.findById(id)
            .orElseThrow { NoSuchElementException("존재하지 않는 구매내역입니다.") }
        return PurchaseServiceResponse.of(purchase)
    }

    override fun findByMemberId(memberId: Long): List<PurchaseServiceResponse> =
        purchaseRepository.findByMember_IdOrderByPurchasedAtDesc(memberId)
            .map { PurchaseServiceResponse.of(it) }

    override fun findByWorkId(workId: Long): List<PurchaseServiceResponse> =
        purchaseRepository.findByWork_IdOrderByPurchasedAtDesc(workId)
            .map { PurchaseServiceResponse.of(it) }

}
