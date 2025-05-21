package com.mirea.dates.controller

import com.mirea.dates.dto.ProfileRequest
import com.mirea.dates.dto.ProfileResponse
import com.mirea.dates.exception.AccessDeniedException
import com.mirea.dates.service.ProfileService
import com.mirea.dates.service.toResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/profiles")
class ProfileController(
    private val profileService: ProfileService
) {
    @PostMapping
    fun createProfile(
        @AuthenticationPrincipal userId: Long,
        @RequestBody request: ProfileRequest
    ): ResponseEntity<ProfileResponse> {
        return ResponseEntity.ok(
            profileService.createProfile(request, userId).toResponse()
        )
    }

    @GetMapping("/me")
    fun getMyProfile(
        @RequestParam userId: Long?,
        @AuthenticationPrincipal principal: String? // Или Long, в зависимости от реализации
    ): ResponseEntity<ProfileResponse> {
        val targetUserId = userId ?: principal?.toLong() // Преобразуем String ID в Long
        ?: throw AccessDeniedException("User not authenticated")
        return ResponseEntity.ok(profileService.getProfileByUserId(targetUserId))
    }

    // Другие endpoint'ы...
}