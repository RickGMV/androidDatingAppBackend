package com.mirea.dates.controller

import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import java.util.UUID
import com.mirea.dates.service.MatchService
import com.mirea.dates.dto.*
import com.mirea.dates.exception.AccessDeniedException
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/matches")
class MatchController(
    private val matchService: MatchService
) {
    @PostMapping
    fun createMatch(
        @AuthenticationPrincipal userId: Long,
        @RequestBody request: MatchRequest
    ): ResponseEntity<MatchResponse> {
        val match = matchService.createMatch(userId, request.targetUserId)
        return ResponseEntity.ok(MatchResponse(match.id, match.matchedAt))
    }

    @PostMapping("/{matchId}/share-telegram")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun shareTelegram(
        @AuthenticationPrincipal userId: Long,
        @PathVariable matchId: UUID
    ) {
        matchService.shareTelegram(matchId, userId)
    }

    @GetMapping
    fun getUserMatches(
        @AuthenticationPrincipal userId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<MatchWithTelegramResponse>> {
        return ResponseEntity.ok(matchService.getUserMatches(userId, page, size))
    }

    @GetMapping("/{matchId}")
    fun getMatchDetails(
        @AuthenticationPrincipal userId: Long,
        @PathVariable matchId: UUID
    ): ResponseEntity<MatchDetailsResponse> {
        return ResponseEntity.ok(matchService.getMatchDetails(matchId, userId))
    }

    @GetMapping("/{matchId}/telegram")
    fun getSharedTelegram(
        @AuthenticationPrincipal userId: Long,
        @PathVariable matchId: UUID
    ): ResponseEntity<String> {
        val status = matchService.getTelegramSharingStatus(matchId, userId)
        if (!status.otherUserShared) {
            throw AccessDeniedException("Other user hasn't shared their Telegram")
        }
        return ResponseEntity.ok(status.otherUserTelegram)
    }

    @GetMapping("/{matchId}/telegram-status")
    fun getTelegramSharingStatus(
        @AuthenticationPrincipal userId: Long,
        @PathVariable matchId: UUID
    ): ResponseEntity<TelegramSharingStatusResponse> {
        return ResponseEntity.ok(matchService.getTelegramSharingStatus(matchId, userId))
    }
}