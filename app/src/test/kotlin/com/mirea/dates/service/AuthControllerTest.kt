package com.mirea.dates.service

import com.mirea.dates.dto.RegistrationRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class AuthControllerTest : BaseIntegrationTest() {

    @Test
    fun `POST login with valid credentials should return token`() {
        // 1. Подготовка запроса
        val request = RegistrationRequest(
            email = "user@example.com",
            password = "password123"
        )

        // 2. Отправка запроса
        val response = restTemplate.exchange(
            "$baseUrl/auth/login",
            HttpMethod.POST,
            HttpEntity(request),
            String::class.java
        )

        // 3. Проверки
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).contains("token")  // Проверяем наличие JWT в ответе
    }
}