package com.homework.comics.member.application.impl

import com.homework.comics.common.authentication.MemberPrincipal
import com.homework.comics.common.component.AuthenticationFacade
import com.homework.comics.common.component.JwtProvider
import com.homework.comics.common.config.PasswordConfig
import com.homework.comics.common.dto.AuthDetails
import com.homework.comics.member.application.MemberQueryService
import com.homework.comics.member.application.dto.AuthServiceResponse
import com.homework.comics.member.application.dto.MemberServiceRequest
import com.homework.comics.member.application.dto.MemberServiceResponse
import com.homework.comics.member.domain.Member
import com.homework.comics.member.repository.MemberRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberQueryServiceImpl(
    private val memberRepository: MemberRepository,
    private val passwordConfig: PasswordConfig,
    private val jwtProvider: JwtProvider,
    private val authenticationFacade: AuthenticationFacade
) : MemberQueryService {

    override fun validateDuplicate(id: Long?, username: String, emailAddress: String) {
        require(!memberRepository.existsByUsername(id, username)) {
            "유저네임이 이미 등록되어 있습니다.입니다. - $username"
        }

        require(!memberRepository.existsByEmailAddress(id, emailAddress)) {
            "이메일주소가 이미 등록되어 있습니다.입니다. - $emailAddress"
        }
    }

    override fun loadUserByUsername(emailAddress: String): AuthDetails {
        val member = fetchMemberByEmailAddress(emailAddress)
        return AuthDetails(
            member.id,
            member.emailAddressValue,
            member.isAdultVerified,
            ArrayList()
        )
    }

    override fun detail(): MemberServiceResponse {
        val memberId = authenticationFacade.getMemberId()
        return MemberServiceResponse.of(fetchById(memberId!!))
    }

    override fun fetchById(id: Long): Member =
        memberRepository.findById(id)
            .orElseThrow { NoSuchElementException("존재하지 않는 회원입니다. - $id") }

    override fun login(memberServiceRequest: MemberServiceRequest): AuthServiceResponse {
        val member = memberServiceRequest.toEntity(passwordConfig.passwordEncoder())
        val existingMember = fetchMemberByEmailAddress(member.emailAddressValue)
        val isValid = passwordConfig.passwordEncoder()
            .matches(memberServiceRequest.password, existingMember.passwordValue)

        if (!isValid) {
            throw BadCredentialsException("로그인정보를 다시 확인해주세요.")
        }

        val memberPrincipal = MemberPrincipal(
            existingMember.id, existingMember.emailAddressValue, existingMember.isAdultVerified
        )

        return AuthServiceResponse(
            accessToken = jwtProvider.generateAccessToken(memberPrincipal),
            refreshToken = jwtProvider.generateRefreshToken(memberPrincipal)
        )
    }

    private fun fetchMemberByEmailAddress(emailAddress: String) =
        memberRepository.findByEmailAddress__value(emailAddress)
            .orElseThrow { NoSuchElementException("로그인정보를 다시 확인해주세요. - $emailAddress") }

    override fun refreshAccessToken(refreshToken: String): AuthServiceResponse {
        val memberId = authenticationFacade.getMemberId()
        val emailAddress = authenticationFacade.getEmailAddress()
        val isAdultVerified = authenticationFacade.isAdultVerified()

        if (!jwtProvider.validateRefreshToken(emailAddress!!, refreshToken)) {
            throw BadCredentialsException("잘못된 호출입니다. 다시 로그인하십시오.")
        }

        val memberPrincipal = MemberPrincipal(memberId!!, emailAddress, isAdultVerified)

        return AuthServiceResponse(
            accessToken = jwtProvider.generateAccessToken(memberPrincipal),
            refreshToken = jwtProvider.generateRefreshToken(memberPrincipal)
        )
    }

    override fun logout(accessToken: String, refreshToken: String) {
        jwtProvider.invalidateRefreshToken(accessToken.removePrefix("Bearer "), refreshToken)
    }

}
