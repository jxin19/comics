package com.homework.comics.purchase.domain

import com.homework.comics.member.domain.Member
import com.homework.comics.work.domain.Work
import com.homework.comics.common.domain.Price
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(
    name = "purchase",
    indexes = [
        Index(name = "idx_purchase_member_date", columnList = "member_id,purchased_at DESC"),
        Index(name = "idx_purchase_work_date", columnList = "work_id,purchased_at DESC")
    ],
    uniqueConstraints = [
        UniqueConstraint(name = "unique_member_work", columnNames = ["member_id", "work_id"])
    ]
)
class Purchase(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id", length = 10, nullable = false, updatable = false, unique = true)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = ForeignKey(name = "fk_purchase_member"))
    private var member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id", nullable = false, foreignKey = ForeignKey(name = "fk_purchase_work"))
    private var work: Work? = null,

    @Embedded
    @AttributeOverride(name = "_value", column = Column(name = "paid_price", nullable = false))
    private var paidPrice: Price? = null,

    @CreatedDate
    @Column(name = "purchased_at", nullable = false, updatable = false)
    private val purchasedAt: Instant = Instant.now()
) {
    val memberId: Long
        get() = member?.id!!

    val workId: Long
        get() = work?.id!!

    val paidPriceValue: BigDecimal
        get() = paidPrice?.value ?: BigDecimal.ZERO

    val purchasedDateTime: Instant
        get() = purchasedAt

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Purchase

        if (id != other.id) return false
        if (member != other.member) return false
        if (work != other.work) return false
        if (paidPrice != other.paidPrice) return false
        if (purchasedAt != other.purchasedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (member?.hashCode() ?: 0)
        result = 31 * result + (work?.hashCode() ?: 0)
        result = 31 * result + (paidPrice?.hashCode() ?: 0)
        result = 31 * result + purchasedAt.hashCode()
        return result
    }

    override fun toString(): String {
        return "Purchase(id=$id, member=$member, work=$work, paidPrice=$paidPrice, purchasedAt=$purchasedAt)"
    }

}
