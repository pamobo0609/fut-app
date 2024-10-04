package com.fut.app.common.api.request

import com.fut.app.entity.UserEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email

@Schema(description = "Request for creating a new user")
data class CreateUserRequest(
    @Schema(examples = ["John", "Jake", "Paul"], required = true)
    val name: String,

    @Schema(examples = ["Doe", "Baggins", "Potter"], required = true)
    val lastName: String,

    @Schema(example = "peter.parker@example.com", required = true)
    @field:Email(message = "Invalid email address", regexp = ".+@.+\\..+")
    val email: String,

    @Schema(example = "StrongPassword123", required = true)
    val password: String
)

fun CreateUserRequest.toEntity() = UserEntity(
    name = name,
    lastName = lastName,
    email = email,
    password = password // TODO: encoding
)

enum class CreateUserResultStatus {
    CREATED,
    INVALID_EMAIL,
    EMAIL_CONFLICT,
    NOT_FOUND
}
