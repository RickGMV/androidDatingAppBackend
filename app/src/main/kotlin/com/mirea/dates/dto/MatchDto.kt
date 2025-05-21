package com.mirea.dates.dto

import java.time.Instant
import java.util.UUID

data class MatchRequest(
    val targetUserId: Long
)

data class MatchResponse(
    val matchId: UUID,
    val matchedAt: Instant
)

data class MatchDetailsResponse(
    val matchId: UUID,
    val profile: MatchedProfileResponse,
    val telegramAvailable: Boolean
)
data class MatchWithTelegramResponse(
    val matchId: UUID,
    val otherUser: MatchUserDto,
    val matchedAt: Instant,
    val telegramAvailable: Boolean,
    val myTelegramShared: Boolean
)

data class MatchUserDto(
    val userId: Long,
    val name: String,
    val avatarUrl: String?
)

data class TelegramSharingStatusResponse(
    val matchId: UUID,
    val currentUserShared: Boolean,
    val otherUserShared: Boolean,
    val otherUserTelegram: String? // null если не открыл
)