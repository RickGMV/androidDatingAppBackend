package com.mirea.dates.config

import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class CustomErrorController : ErrorController {

    @RequestMapping("/error")
    fun handleError(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("""
                Resource not found. 
                Try <a href='/'>home page</a> or 
                <a href='/logout'>login with another account</a>
            """.trimIndent())
    }
}