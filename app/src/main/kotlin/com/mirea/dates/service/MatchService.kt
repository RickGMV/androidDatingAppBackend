package com.mirea.dates.service

import com.mirea.dates.exception.ConflictException
import com.mirea.dates.model.Match
import com.mirea.dates.repository.MatchRepository
import com.mirea.dates.dto.*
import com.mirea.dates.exception.AccessDeniedException
import com.mirea.dates.repository.UserRepository
import com.mirea.dates.repository.findByIdOrThrow
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class MatchService(
    private val matchRepository: MatchRepository,
    private val profileService: ProfileService,
    private val userRepository: UserRepository
) {

    @Transactional
    fun createMatch(user1Id: Long, user2Id: Long): Match {
        if (user1Id == user2Id) {
            throw IllegalArgumentException("Cannot create a match with the same user.")
        }

        if (matchRepository.existsByUser1IdAndUser2Id(user1Id, user2Id) ||
            matchRepository.existsByUser1IdAndUser2Id(user2Id, user1Id)
        ) {
            throw ConflictException("Match already exists between these users.")
        }

        val user1 = userRepository.findById(user1Id).orElseThrow {
            EntityNotFoundException("User with ID $user1Id not found")
        }

        val user2 = userRepository.findById(user2Id).orElseThrow {
            EntityNotFoundException("User with ID $user2Id not found")
        }

        return matchRepository.save(Match(user1 = user1, user2 = user2))
    }

    @Transactional
    fun shareTelegram(matchId: UUID, requestingUserId: Long) {
        val match = matchRepository.findByIdOrThrow(matchId)

        when {
            match.user1.id == requestingUserId -> {
                match.telegramSharedByUser1 = true
                match.user1SharedAt = Instant.now()
            }

            match.user2.id == requestingUserId -> {
                match.telegramSharedByUser2 = true
                match.user2SharedAt = Instant.now()
            }

            else -> throw AccessDeniedException("User not part of this match")
        }

        matchRepository.save(match)
    }

    @Transactional(readOnly = true)
    fun getMatchDetails(matchId: UUID, userId: Long): MatchDetailsResponse {
        val match = matchRepository.findByIdOrThrow(matchId)

        if (!match.containsUser(userId)) {
            throw AccessDeniedException("User not part of this match")
        }

        val isUser1 = match.user1.id == userId
        val otherUserId = if (isUser1) match.user2.id else match.user1.id
        val profile = profileService.getProfileForMatch(otherUserId!!)

        return MatchDetailsResponse(
            matchId = match.id,
            profile = profile,
            telegramAvailable = if (isUser1) {
                match.telegramSharedByUser2
            } else {
                match.telegramSharedByUser1
            }
        )
    }

    @Transactional(readOnly = true)
    fun getUserMatches(userId: Long, page: Int, size: Int): Page<MatchWithTelegramResponse> {
        val pageable = PageRequest.of(
            page,
            size,
            Sort.by(Sort.Direction.DESC, "matchedAt")
        )

        return matchRepository.findByUser1IdOrUser2Id(userId, userId, pageable).map { match ->
            val isUser1 = match.user1.id == userId
            val otherUser = if (isUser1) match.user2 else match.user1
            val otherProfile = profileService.getProfileForMatch(otherUser.id!!)

            MatchWithTelegramResponse(
                matchId = match.id,
                otherUser = MatchUserDto(
                    userId = otherUser.id,
                    name = otherProfile.nickname,
                    avatarUrl = otherProfile.photoUrls.firstOrNull()
                ),
                matchedAt = match.matchedAt,
                telegramAvailable = isUser1 && match.telegramSharedByUser2 ||
                        !isUser1 && match.telegramSharedByUser1,
                myTelegramShared = if (isUser1) match.telegramSharedByUser1
                else match.telegramSharedByUser2
            )
        }
    }
    @Transactional(readOnly = true)
    fun getTelegramSharingStatus(matchId: UUID, userId: Long): TelegramSharingStatusResponse {
        val match = matchRepository.findByIdOrThrow(matchId)

        if (!match.containsUser(userId)) {
            throw AccessDeniedException("User not part of this match")
        }

        val isUser1 = match.user1.id == userId
        val otherUserShared =
            if (isUser1) match.telegramSharedByUser2 else match.telegramSharedByUser1
        val otherUser = if (isUser1) match.user2 else match.user1
        val otherProfile = profileService.getProfileForMatch(otherUser.id!!)

        return TelegramSharingStatusResponse(
            matchId = match.id,
            currentUserShared = if (isUser1) match.telegramSharedByUser1 else match.telegramSharedByUser2,
            otherUserShared = otherUserShared,
            otherUserTelegram = if (otherUserShared) otherProfile.telegramId else null
        )
    }
}