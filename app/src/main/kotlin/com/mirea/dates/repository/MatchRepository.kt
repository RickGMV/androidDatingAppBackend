package com.mirea.dates.repository

import com.mirea.dates.model.Match
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface MatchRepository : JpaRepository<Match, UUID> {
    fun findByUser1IdOrUser2Id(
        user1Id: Long,
        user2Id: Long,
        pageable: Pageable
    ): Page<Match>
    fun existsByUser1IdAndUser2Id(user1Id: Long, user2Id: Long): Boolean

    @Query("""
    SELECT m FROM Match m 
    WHERE (m.user1.id = :userId OR m.user2.id = :userId) 
    AND (m.telegramSharedByUser1 = true OR m.telegramSharedByUser2 = true)
""")
    fun findSharedMatches(userId: Long): List<Match>
}