package com.homework.comics.member.application

import com.homework.comics.common.dto.AuthDetails
import com.homework.comics.member.application.dto.AuthServiceResponse
import com.homework.comics.member.application.dto.MemberServiceRequest
import com.homework.comics.member.application.dto.MemberServiceResponse
import com.homework.comics.member.domain.Member
import org.springframework.security.core.userdetails.UserDetailsService

interface MemberQueryService : UserDetailsService {
    fun validateDuplicate(id: Long? = null, username: String, emailAddress: String)
    override fun loadUserByUsername(emailAddress: String): AuthDetails
    fun detail(): MemberServiceResponse
    fun fetchById(id: Long): Member
    fun login(memberServiceRequest: MemberServiceRequest): AuthServiceResponse
    fun refreshAccessToken(refreshToken: String): AuthServiceResponse
    fun logout(accessToken: String, refreshToken: String)
}
