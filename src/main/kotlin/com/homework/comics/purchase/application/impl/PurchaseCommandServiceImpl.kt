package com.homework.comics.purchase.application.impl

import com.homework.comics.common.component.AuthenticationFacade
import com.homework.comics.common.domain.Price
import com.homework.comics.common.exception.AdultOnlyAccessDeniedException
import com.homework.comics.member.domain.Member
import com.homework.comics.purchase.application.PurchaseCommandService
import com.homework.comics.purchase.application.dto.PurchaseServiceResponse
import com.homework.comics.purchase.domain.Purchase
import com.homework.comics.purchase.repository.PurchaseRepository
import com.homework.comics.work.application.dto.WorkServiceResponse
import com.homework.comics.work.domain.Work
import com.homework.comics.workPricingPolicy.application.WorkPricingPolicyQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PurchaseCommandServiceImpl(
    private val purchaseRepository: PurchaseRepository,
    private val workPricingPolicyQueryService: WorkPricingPolicyQueryService,
    private val authenticationFacade: AuthenticationFacade
) : PurchaseCommandService {

    override fun purchase(workId: Long): PurchaseServiceResponse {
        val memberId = authenticationFacade.getMemberId()
        val workPricingPolicyServiceResponse = workPricingPolicyQueryService.findCurrentPolicyByWorkId(workId)

        validateAdultOnly(workPricingPolicyServiceResponse.workServiceResponse)

        if (workPricingPolicyServiceResponse.free) {
            throw IllegalArgumentException("무료 작품은 구매하지 않고 이용가능합니다.")
        }

        val purchase = Purchase(
            member = Member(id = memberId!!),
            work = Work(id = workPricingPolicyServiceResponse.workId),
            paidPrice = Price(workPricingPolicyServiceResponse.price)
        )

        return PurchaseServiceResponse.of(purchaseRepository.save(purchase))
    }

    private fun validateAdultOnly(workServiceResponse: WorkServiceResponse) {
        if (workServiceResponse.adultOnly && !authenticationFacade.isAdultVerified()) {
            throw AdultOnlyAccessDeniedException("성인인증 후 이용 가능한 작품입니다.")
        }
    }

}
