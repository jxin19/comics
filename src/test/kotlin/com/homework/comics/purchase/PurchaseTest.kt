package com.homework.comics.purchase

import com.homework.comics.common.domain.Price
import com.homework.comics.member.domain.Member
import com.homework.comics.member.domain.property.EmailAddress
import com.homework.comics.member.domain.property.Password
import com.homework.comics.member.domain.property.Username
import com.homework.comics.purchase.domain.Purchase
import com.homework.comics.work.domain.Work
import com.homework.comics.work.domain.property.Author
import com.homework.comics.work.domain.property.Description
import com.homework.comics.work.domain.property.WorkTitle
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigDecimal

@SpringBootTest
class PurchaseTest {

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    private fun createValidMember() = Member(
        username = Username("user"),
        emailAddress = EmailAddress("test@test.com"),
        password = Password("password123!", passwordEncoder)
    )

    private fun createValidWork() = Work(
        title = WorkTitle("작품"),
        description = Description("설명"),
        author = Author("작가")
    )

    @Test
    fun `구매 생성 테스트`() {
        val purchase = Purchase(
            member = createValidMember(),
            work = createValidWork(),
            paidPrice = Price(BigDecimal("1000"))
        )

        assertEquals(BigDecimal("1000.00"), purchase.paidPriceValue)
        assertNotNull(purchase.purchasedDateTime)
    }

    @Test
    fun `구매 가격 0원 미만 실패`() {
        assertThrows<IllegalArgumentException>("가격은 0 이상이어야 합니다.") {
            Purchase(
                member = createValidMember(),
                work = createValidWork(),
                paidPrice = Price(BigDecimal("-1000"))
            )
        }
    }

}
