package com.homework.comics.member.domain

import com.homework.comics.common.domain.BaseEntity
import com.homework.comics.member.domain.property.AdultVerified
import com.homework.comics.member.domain.property.EmailAddress
import com.homework.comics.member.domain.property.Password
import com.homework.comics.member.domain.property.Username
import jakarta.persistence.*

@Entity
@Table(
    name = "member",
    indexes = [
        Index(name = "idx_member_username", columnList = "username")
    ]
)
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", length = 10, nullable = false, updatable = false, unique = true)
    val id: Long = 0,

    @Embedded
    private var username: Username? = null,

    @Embedded
    private var emailAddress: EmailAddress? = null,

    @Embedded
    private var password: Password? = null,

    private var adultVerified: AdultVerified? = null,
) : BaseEntity() {

    val usernameValue: String
        get() = username?.value ?: ""

    val emailAddressValue: String
        get() = emailAddress?.value ?: ""

    val passwordValue: String
        get() = password?.value ?: ""

    val isAdultVerified: Boolean
        get() = adultVerified?._value ?: false

    fun update(other: Member) {
        this.username = other.username
        this.emailAddress = other.emailAddress
        this.password = other.password
    }

    fun verifyAdult(identityCheckValue: String) {
        if (this.adultVerified == null || this.adultVerified?._value == false) {
            this.adultVerified = AdultVerified(identityCheckValue)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        if (id != other.id) return false
        if (username != other.username) return false
        if (emailAddress != other.emailAddress) return false
        if (password != other.password) return false
        if (adultVerified != other.adultVerified) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + (emailAddress?.hashCode() ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (adultVerified?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Member(id=$id, username=$username, emailAddress=$emailAddress, password=$password, adultVerified=$adultVerified)"
    }

}
