package com.mirea.dates.repository

import com.mirea.dates.exception.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository

fun <T, ID> JpaRepository<T, ID>.findByIdOrThrow(id: ID): T where ID : Any {
    return findById(id).orElseThrow { NotFoundException("Entity with id $id not found") }
}