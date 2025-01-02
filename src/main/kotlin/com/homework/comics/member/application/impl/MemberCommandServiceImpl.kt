package com.homework.comics.member.application.impl

import com.homework.comics.common.component.AuthenticationFacade
import com.homework.comics.common.config.PasswordConfig
import com.homework.comics.member.application.MemberCommandService
import com.homework.comics.member.application.MemberQueryService
import com.homework.comics.member.application.dto.MemberServiceRequest
import com.homework.comics.member.application.dto.MemberServiceResponse
import com.homework.comics.member.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberCommandServiceImpl(
    private val memberRepository: MemberRepository,
    private val memberQueryService: MemberQueryService,
    private val passwordConfig: PasswordConfig,
    private val authenticationFacade: AuthenticationFacade
) : MemberCommandService {

    override fun create(memberServiceRequest: MemberServiceRequest): MemberServiceResponse {
        val newMember = memberServiceRequest.toEntity(passwordConfig.passwordEncoder())

        memberQueryService.validateDuplicate(
            username = memberServiceRequest.username,
            emailAddress = memberServiceRequest.emailAddress
        )

        val savedMember = memberRepository.save(newMember)

        return MemberServiceResponse.of(savedMember)
    }

    override fun update(memberServiceRequest: MemberServiceRequest): MemberServiceResponse {
        val memberId = authenticationFacade.getMemberId()
        val member = memberQueryService.fetchById(memberId!!)

        memberQueryService.validateDuplicate(
            id = memberId,
            username = memberServiceRequest.username,
            emailAddress = memberServiceRequest.emailAddress
        )

        val updateMember = memberServiceRequest.toEntity(passwordConfig.passwordEncoder())

        member.update(updateMember)

        return MemberServiceResponse.of(member)
    }

    override fun verifyAdult(identityCheckValue: String): MemberServiceResponse {
        val memberId = authenticationFacade.getMemberId()
        val member = memberQueryService.fetchById(memberId!!)
        member.verifyAdult(identityCheckValue)

        return MemberServiceResponse.of(member)
    }

}
