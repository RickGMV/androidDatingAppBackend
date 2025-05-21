package com.mirea.dates.model

import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(indexes = [
    Index(name = "idx_match_user1", columnList = "user1_id"),
    Index(name = "idx_match_user2", columnList = "user2_id")
])
data class Match(
    @Id val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    val user1: User,

    @ManyToOne(fetch = FetchType.LAZY)
    val user2: User,

    val matchedAt: Instant = Instant.now(),

    var telegramSharedByUser1: Boolean = false,
    var telegramSharedByUser2: Boolean = false,

    @Column
    var user1SharedAt: Instant? = null,

    @Column
    var user2SharedAt: Instant? = null
) {
    fun containsUser(userId: Long): Boolean {
        return user1.id == userId || user2.id == userId
    }

    fun hasUserSharedTelegram(userId: Long): Boolean {
        return when (userId) {
            user1.id -> telegramSharedByUser1
            user2.id -> telegramSharedByUser2
            else -> false
        }
    }
}