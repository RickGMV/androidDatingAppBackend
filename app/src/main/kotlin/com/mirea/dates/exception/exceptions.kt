package com.mirea.dates.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.CONFLICT)
class ConflictException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ValidationException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.FORBIDDEN)
class AccessDeniedException(message: String) : RuntimeException(message)