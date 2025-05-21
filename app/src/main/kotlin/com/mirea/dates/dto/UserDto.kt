package com.mirea.dates.dto

import jakarta.validation.constraints.*
import java.util.UUID

data class UserRegisterRequest(
    @field:NotBlank
    @field:Email
    @field:Size(max = 255)
    val email: String,

    @field:NotBlank
    @field:Size(min = 8, max = 100)
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
        message = "Password must contain at least 1 digit, 1 lowercase and 1 uppercase letter"
    )
    val password: String,

    @field:NotBlank
    @field:Size(min = 2, max = 50)
    val firstName: String,

    @field:NotBlank
    @field:Size(min = 2, max = 50)
    val lastName: String
)

data class UserLoginRequest(
    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    val password: String
)

data class UserResponse(
    val id: UUID,
    val email: String,
    val isVerified: Boolean
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResponse
)

data class UserUpdateRequest(
    @field:Size(min = 2, max = 50)
    val firstName: String?,

    @field:Size(min = 2, max = 50)
    val lastName: String?,

    @field:Size(max = 1000)
    val bio: String?
)