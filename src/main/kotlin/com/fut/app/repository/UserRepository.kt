package com.fut.app.repository

import com.fut.app.entity.UserEntity
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserEntity, Long> {

    fun existsByEmail(email: String): Boolean
}
