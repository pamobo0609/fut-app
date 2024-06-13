package com.fut.app.controller.user

import com.fut.app.common.*
import com.fut.app.common.api.APIError
import com.fut.app.common.api.APIResult.Failure
import com.fut.app.common.api.APIResult.Success
import com.fut.app.common.api.request.CreateUserRequest
import com.fut.app.common.api.request.CreateUserResultStatus
import com.fut.app.model.User
import com.fut.app.service.user.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(USER_BASE_MAPPING)
@LogDiagnostics
class UserController(
    private val userService: UserService,
) {

    @GetMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Get all users",
        description = "Gets all the users from the database",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(schema = Schema(implementation = User::class))]
            )
        ]
    )
    fun getAllUsers(): ResponseEntity<APIDocument<List<User>>> = ResponseEntity(userService.getAllUsers().toAPIDocument(), HttpStatus.OK)

    @PostMapping(
        value = [CREATE_USER_MAPPING],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Creates a new user",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(schema = Schema(implementation = User::class))]
            )
        ]
    )
    fun createUser(
        @Parameter(description = "JSON object defining the user to create", required = true) @Valid @RequestBody request: CreateUserRequest
    ) = when (val result = userService.createUser(request)) {
        is Success -> {
            logger.info("User created successfully: ${result.value}")
            result.value.toAPIDocument().toResponseEntity()
        }
        is Failure -> handleUserCreationFailure(result.error)
    }

    @PutMapping(
        value = ["${UPDATE_USER_MAPPING}/{id}"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Creates a new user",
        responses = [
            ApiResponse(
                responseCode = "200",
                content = [Content(schema = Schema(implementation = User::class))]
            ),
            ApiResponse(
                responseCode = "404",
                content = [Content(schema = Schema(implementation = APIDocument::class))]
            ),
            ApiResponse(
                responseCode = "400",
                content = [Content(schema = Schema(implementation = APIDocument::class))]
            ),
        ]
    )
    fun updateUser(
        @PathVariable(value = "id") @NotBlank @Min(value = ZERO.toLong(), message = "An id must be a positive number") id: Long,
        @Parameter(description = "JSON object defining the user to update", required = true) @Valid @RequestBody request: CreateUserRequest
    ) = when (val result = userService.updateUser(id, request)) {
        is Success -> {
            logger.info("User updated successfully: ${result.value}")
            result.value.toAPIDocument().toResponseEntity()
        }
        is Failure -> handleUserCreationFailure(result.error)
    }

    private fun handleUserCreationFailure(status: CreateUserResultStatus) = when(status) {
        CreateUserResultStatus.INVALID_EMAIL -> ResponseEntity(
            APIDocument<User>(
                data = null,
                errors = listOf(
                    APIError(
                        code = HttpStatus.BAD_REQUEST.value(),
                        title = "Invalid email address.",
                        detail = "Invalid email address."
                    )
                )
            ), HttpStatus.BAD_REQUEST
        )
        CreateUserResultStatus.EMAIL_CONFLICT -> ResponseEntity(
            APIDocument<User>(
                data = null,
                errors = listOf(
                    APIError(
                        code = HttpStatus.CONFLICT.value(),
                        title = "Email address already in use.",
                        detail = "Email address already in use."
                    )
                )
            ), HttpStatus.CONFLICT
        )
        CreateUserResultStatus.NOT_FOUND -> ResponseEntity(
            APIDocument<User>(
                data = null,
                errors = listOf(
                    APIError(
                        code = HttpStatus.NOT_FOUND.value(),
                        title = "User not found.",
                        detail = "User not found in the database."
                    )
                )
            ), HttpStatus.NOT_FOUND
        )
        else -> ResponseEntity(
            APIDocument<User>(
                data = null,
                errors = listOf(
                    APIError(
                        code = HttpStatus.BAD_REQUEST.value(),
                        title = "Unexpected error.",
                        detail = "Unexpected error while creating user"
                    )
                )
            ), HttpStatus.BAD_REQUEST
        )
    }

    private companion object {
        private val logger = LoggerFactory.getLogger(UserController::class.java)
    }
}
