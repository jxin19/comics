package com.homework.comics.common.authentication

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AuthenticationToken : AbstractAuthenticationToken {
    private val memberPrincipal: MemberPrincipal

    constructor(memberPrincipal: MemberPrincipal, authorities: Collection<GrantedAuthority>) : super(authorities) {
        this.memberPrincipal = memberPrincipal
        isAuthenticated = true
    }

    override fun getCredentials(): Any? = null

    override fun getPrincipal(): Any = memberPrincipal
}
