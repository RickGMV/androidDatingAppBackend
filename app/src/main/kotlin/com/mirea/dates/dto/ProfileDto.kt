package com.mirea.dates.dto

import jakarta.validation.constraints.*


data class ProfileRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 30)
    val nickname: String,

    @field:Min(18)
    @field:Max(100)
    val age: Int,

    @field:Size(max = 1000)
    val description: String,

    @field:Size(min = 1, max = 3)
    val photoUrls: List<@NotBlank String>,


    @field:NotBlank
    @field:Pattern(regexp = "^@[a-zA-Z0-9_]{5,32}$")
    val telegramId: String
)


data class ProfileResponse(
    val id: Long,
    val nickname: String,
    val age: Int,
    val description: String,
    val photoUrls: List<String>
)
data class ProfileUpdateRequest(
    @field:Size(max = 250)
    val description: String,

    @field:Size(min = 1, max = 3)
    val photoUrls: List<@NotBlank String>

)


data class MatchedProfileResponse(
    val id: Long,
    val nickname: String,
    val photoUrls: List<String>,
    val telegramId: String)