package com.fut.app.utils.user

import com.fut.app.common.api.request.CreateUserRequest
import com.fut.app.entity.UserEntity
import com.fut.app.model.User
import com.fut.app.utils.randomEmail
import com.fut.app.utils.randomLongNonNegative
import com.fut.app.utils.randomString

fun stubbedUserEntity(
     id: Long = randomLongNonNegative(),
     name: String = randomString(),
     lastName: String = randomString(),
     email: String = randomEmail()
) = UserEntity(
    id = id,
    name = name,
    lastName = lastName,
    email = email,
)

fun stubbedCreateUserRequest(
    name: String = randomString(),
    lastName: String = randomString(),
    email: String = randomEmail(),
    password: String = randomString()
) = CreateUserRequest(
    name = name,
    lastName = lastName,
    email = email,
    password = password
)

fun stubbedUser(
    id: Long = randomLongNonNegative(),
    name: String = randomString(),
    lastName: String = randomString(),
    email: String = randomEmail()
) = User(
    id = id,
    name = name,
    lastName = lastName,
    email = email
)