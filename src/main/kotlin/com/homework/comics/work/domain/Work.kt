package com.homework.comics.work.domain

import com.homework.comics.common.domain.BaseEntity
import com.homework.comics.work.domain.property.Author
import com.homework.comics.work.domain.property.Description
import com.homework.comics.work.domain.property.WorkTitle
import jakarta.persistence.*

@Entity
@Table(
    name = "work",
    indexes = [
        Index(name = "idx_work_title", columnList = "title")
    ]
)
class Work(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id", length = 10, nullable = false, updatable = false, unique = true)
    val id: Long = 0,

    @Embedded
    private var title: WorkTitle? = null,

    @Embedded
    private var description: Description? = null,

    @Embedded
    private var author: Author? = null,

    @Column(name = "is_active")
    private var _isActive: Boolean = true,

    @Column(name = "is_adult_only")
    private var _isAdultOnly: Boolean = false,
) : BaseEntity() {

    val titleValue: String
        get() = title?.value ?: ""

    val descriptionValue: String
        get() = description?.value ?: ""

    val authorValue: String
        get() = author?.value ?: ""

    val isActive: Boolean
        get() = _isActive

    val isAdultOnly: Boolean
        get() = _isAdultOnly

    fun update(other: Work) {
        this.title = other.title
        this.description = other.description
        this.author = other.author
        this._isActive = other._isActive
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Work

        if (id != other.id) return false
        if (_isActive != other._isActive) return false
        if (_isAdultOnly != other._isAdultOnly) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (author != other.author) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + _isActive.hashCode()
        result = 31 * result + _isAdultOnly.hashCode()
        result = 31 * result + (title?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (author?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Work(id=$id, title=$title, description=$description, author=$author, _isActive=$_isActive, _isAdultOnly=$_isAdultOnly)"
    }

}
