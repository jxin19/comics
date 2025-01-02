package com.homework.comics.member.repository

import com.homework.comics.member.domain.Member
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface MemberRepository : CrudRepository<Member, Long> {
    @Query("""
       SELECT EXISTS (
           SELECT 1 FROM Member m 
           WHERE (:id IS NULL OR m.id != :id) 
           AND m.username._value = :username
       )
    """)
    fun existsByUsername(id: Long?, username: String): Boolean

    @Query("""
       SELECT EXISTS (
           SELECT 1 FROM Member m 
           WHERE (:id IS NULL OR m.id != :id) 
           AND m.emailAddress._value = :emailAddress
       )
    """)
    fun existsByEmailAddress(id: Long?, emailAddress: String): Boolean

    fun findByEmailAddress__value(emailAddress: String): Optional<Member>
}
