package com.mirea.dates.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size

@Entity
@Table(
    name = "profiles",
    indexes = [
        Index(name = "idx_profile_nickname", columnList = "nickname", unique = true),
        Index(name = "idx_profile_user", columnList = "user_id")
    ]
)
data class Profile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 30)
    val nickname: String,

    @Column(nullable = false)
    val age: Int,

    @Column(length = 1000)
    val description: String,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "profile_photos",
        joinColumns = [JoinColumn(name = "profile_id")],
        foreignKey = ForeignKey(name = "fk_profile_photos")
    )
    @Column(name = "photo_url", nullable = false)
    @field:Size(max = 3)
    val photoUrls: List<String>,

    @Column(name = "telegram_id", length = 32)
    val telegramId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = ForeignKey(name = "fk_profile_user")
    )
    val user: User
) {
    /**
     * Валидация данных перед сохранением
     */
    @PrePersist
    @PreUpdate
    fun validate() {
        require(photoUrls.size <= 3) { "Maximum 3 photos allowed" }
        require(age in 16..100) { "Age must be between 16 and 100" }
    }
}