package com.homework.comics.workPricingPolicy.domain

import com.homework.comics.common.domain.BaseEntity
import com.homework.comics.work.domain.Work
import com.homework.comics.common.domain.Price
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(
    name = "work_pricing_policy",
    indexes = [
        Index(name = "idx_work_pricing_policy_work_id_date", columnList = "work_id,start_date,end_date")
    ]
)
class WorkPricingPolicy(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_pricing_policy_id", length = 10, nullable = false, updatable = false, unique = true)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id", nullable = false, foreignKey = ForeignKey(name = "fk_work_pricing_policy_work"))
    private var work: Work? = null,

    @Column(name = "is_free", nullable = false)
    private var _isFree: Boolean = false,

    @Embedded
    private var price: Price? = null,

    @Column(name = "start_date", nullable = false)
    private var startDate: Instant,

    @Column(name = "end_date")
    private var endDate: Instant? = null,
) : BaseEntity() {
    init {
        require(endDate == null || startDate.isBefore(endDate)) {
            "종료일은 시작일보다 이후여야 합니다."
        }
    }

    val workEntity: Work
        get() = work!!

    val workId: Long
        get() = work?.id!!

    val isFree: Boolean
        get() = _isFree

    val startAt: Instant
        get() = startDate

    val endAt: Instant?
        get() = endDate

    val priceValue: BigDecimal
        get() = price?.value ?: BigDecimal.ZERO

    fun update(other: WorkPricingPolicy) {
        this._isFree = other._isFree
        this.price = other.price
        this.startDate = other.startDate
        this.endDate = other.endDate
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WorkPricingPolicy

        if (id != other.id) return false
        if (_isFree != other._isFree) return false
        if (work != other.work) return false
        if (price != other.price) return false
        if (startDate != other.startDate) return false
        if (endDate != other.endDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + _isFree.hashCode()
        result = 31 * result + work.hashCode()
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + startDate.hashCode()
        result = 31 * result + (endDate?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "WorkPricingPolicy(id=$id, work=$work, _isFree=$_isFree, price=$price, startAt=$startDate, endAt=$endDate)"
    }

}
