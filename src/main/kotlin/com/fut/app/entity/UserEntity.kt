package com.fut.app.entity

import com.fut.app.common.EMPTY_STRING
import com.fut.app.model.User
import jakarta.persistence.*
import jakarta.validation.constraints.Email

@Entity
@Table(name = "users")
data class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = EMPTY_STRING,

    val lastName: String = EMPTY_STRING,

    @Column(unique = true)
    @Email
    val email: String = EMPTY_STRING,

    val password: String = EMPTY_STRING
)

fun UserEntity.toUser() = User(id, name, lastName, email)
