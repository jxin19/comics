package com.homework.comics.member.application

import com.homework.comics.member.application.dto.MemberServiceRequest
import com.homework.comics.member.application.dto.MemberServiceResponse

interface MemberCommandService {
    fun create(memberServiceRequest: MemberServiceRequest): MemberServiceResponse
    fun update(memberServiceRequest: MemberServiceRequest): MemberServiceResponse
    fun verifyAdult(identityCheckValue: String): MemberServiceResponse
}
