package com.mirea.dates.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { requests ->
                requests
                    .requestMatchers("/", "/public/**").permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { form ->
                form
                    .defaultSuccessUrl("/api/profiles/me") // Перенаправляем сюда после входа
                    .permitAll()
            }
            .logout { logout ->
                logout
                    .logoutSuccessUrl("/")
                    .permitAll()
            }
        return http.build()
    }
}