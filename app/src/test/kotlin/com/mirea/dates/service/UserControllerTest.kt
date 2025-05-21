package com.mirea.dates.service

import com.mirea.dates.dto.UserLoginRequest
import com.mirea.dates.dto.UserResponse
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat

class UserControllerTest : BaseIntegrationTest() {

    @Test
    fun `GET me with valid token should return user data`() {
        // 1. Аутентификация и получение токена
        val token = authenticateUser(
            email = "student@mirea.ru",
            password = "securePassword"
        )

        // 2. Формируем запрос с токеном
        val headers = authHeader(token)
        val request = HttpEntity<Any>(headers) // Пустое тело, только заголовки

        // 3. Отправляем запрос
        val response = restTemplate.exchange(
            "$baseUrl/users/me",
            HttpMethod.GET,
            request,
            UserResponse::class.java // Ожидаем объект UserResponse
        )

        // 4. Проверяем результат
        assertThat(response.statusCode)
            .`as`("Проверка статус-кода")
            .isEqualTo(HttpStatus.OK)

        assertThat(response.body?.email)
            .`as`("Проверка email пользователя")
            .isEqualTo("student@mirea.ru")

    }

    private fun authenticateUser(email: String, password: String): String {
        val loginResponse = restTemplate.postForEntity(
            "$baseUrl/auth/login",
            UserLoginRequest(email, password),
            Map::class.java
        )
        return loginResponse.body?.get("access_token") as String
    }
}