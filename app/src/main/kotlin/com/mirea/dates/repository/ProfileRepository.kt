package com.mirea.dates.repository

import com.mirea.dates.model.Profile
import org.springframework.data.jpa.repository.JpaRepository

interface ProfileRepository : JpaRepository<Profile, Long> {
    fun findByNickname(nickname: String): Profile?
    fun findByUserId(userId: Long?): Profile?
}