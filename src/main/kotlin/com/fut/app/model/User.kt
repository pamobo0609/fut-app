package com.fut.app.model

import com.fut.app.common.SPACE
import com.fut.app.entity.UserEntity

data class User(
    val id: Long,
    val name: String,
    val lastName: String,
    val email: String,
)

fun User.displayName() = name + SPACE + lastName

fun User.toEntity() = UserEntity(
    id = this.id,
    name = this.name,
    lastName = this.lastName,
    email = this.email,
)