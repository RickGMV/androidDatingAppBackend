package com.mirea.dates.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

// Регистрация пользователя
data class RegistrationRequest(
    @field:Email
    val email: String,

    @field:Size(min = 8, max = 64)
    val password: String
)

