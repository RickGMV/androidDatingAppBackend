package com.mirea.dates.service

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // Используем профиль 'test'
abstract class BaseIntegrationTest {

    @LocalServerPort
    protected var port: Int = 0

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    protected lateinit var baseUrl: String

    @BeforeEach
    fun setUp() {
        baseUrl = "http://localhost:$port/api/v1"  // Базовый URL API
    }

    protected fun authHeader(token: String): HttpHeaders {
        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer $token")
        return headers
    }
}