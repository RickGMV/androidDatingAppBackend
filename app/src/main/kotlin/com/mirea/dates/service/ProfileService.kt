package com.mirea.dates.service

import com.mirea.dates.dto.*
import com.mirea.dates.exception.NotFoundException
import com.mirea.dates.model.Profile
import com.mirea.dates.repository.ProfileRepository
import com.mirea.dates.repository.UserRepository
import com.mirea.dates.repository.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileService(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository,
) {
    /**
     * Создает новый профиль с шифрованием Telegram ID
     */
    @Transactional
    fun createProfile(request: ProfileRequest, userId: Long): Profile {
        return profileRepository.save(
            Profile(
                nickname = request.nickname,
                age = request.age,
                description = request.description,
                photoUrls = request.photoUrls,
                telegramId = request.telegramId,
                user = userRepository.getReferenceById(userId)
            )
        )
    }

    @Transactional
    fun updateProfile(
        userId: Long,
        request: ProfileUpdateRequest
    ): ProfileResponse {
        val profile = profileRepository.findByUserId(userId)
            ?: throw NotFoundException("Profile not found")

        return profileRepository.save(
            profile.copy(
                description = request.description.take(250),
                photoUrls = request.photoUrls.take(3)
            )
        ).toResponse()
    }


    @Transactional(readOnly = true)
    fun getProfileForMatch(profileId: Long): MatchedProfileResponse {
        val profile = profileRepository.findByIdOrThrow(profileId)
        return MatchedProfileResponse(
            id = profile.id!!,
            nickname = profile.nickname,
            photoUrls = profile.photoUrls,
            telegramId = profile.telegramId
        )
    }

    @Transactional(readOnly = true)
    fun getProfileByUserId(userId: Long?): ProfileResponse {
        val profile = profileRepository.findByUserId(userId)
            ?: throw NotFoundException("Profile not found for user $userId")

        return ProfileResponse(
            id = profile.id!!,
            nickname = profile.nickname,
            age = profile.age,
            description = profile.description,
            photoUrls = profile.photoUrls
        )
    }

    // Другие методы...
}
fun Profile.toResponse() = ProfileResponse(
    id = id!!,
    nickname = nickname,
    age = age,
    description = description,
    photoUrls = photoUrls
)