package com.fut.app.controller

import com.fut.app.common.APIDocument
import com.fut.app.common.api.APIError
import com.fut.app.common.api.APIResult.Failure
import com.fut.app.common.api.APIResult.Success
import com.fut.app.common.api.request.CreateUserResultStatus
import com.fut.app.common.toAPIDocument
import com.fut.app.controller.user.UserController
import com.fut.app.entity.UserEntity
import com.fut.app.model.User
import com.fut.app.repository.UserRepository
import com.fut.app.service.user.UserService
import com.fut.app.utils.randomLongNonNegative
import com.fut.app.utils.user.stubbedCreateUserRequest
import com.fut.app.utils.user.stubbedUser
import com.fut.app.utils.user.stubbedUserEntity
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


class UserControllerTest {

    private val userService: UserService = mockk()
    private val userRepository: UserRepository = mockk()
    private val userController = UserController(userService)

    private val stubbedUserEntity: UserEntity = stubbedUserEntity()

    private val stubbedUser: User = stubbedUser()
    private val stubbedUserId: Long = randomLongNonNegative()
    private val stubbedCreateUserRequest = stubbedCreateUserRequest()

    @BeforeEach
    fun before() {
        every { userService.getAllUsers() } returns listOf(stubbedUser)
        every { userService.createUser(any()) } returns Success(stubbedUser)
        every { userService.updateUser(any(), any()) } returns Success(stubbedUser)
        every { userRepository.findByIdOrNull(any()) } returns stubbedUserEntity
    }

    @Test
    fun `WHEN getting all users THEN 200`() {
        val expected = ResponseEntity(userService.getAllUsers().toAPIDocument(), HttpStatus.OK)
        val actual = userController.getAllUsers()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN getting all users AND no users then 200 AND empty list`() {
        every { userService.getAllUsers() } returns emptyList()

        val expected = ResponseEntity(emptyList<User>().toAPIDocument(), HttpStatus.OK)
        val actual = userController.getAllUsers()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN creating a user THEN 200`() {
        val expected = stubbedUser.toAPIDocument().toResponseEntity()
        val actual = userController.createUser(stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN creating a user with conflicting email THEN 409`() {
        every { userService.createUser(any()) } returns Failure(CreateUserResultStatus.EMAIL_CONFLICT)

        val expected = ResponseEntity(
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
        val actual = userController.createUser(stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN creating a user with bad email THEN 400`() {
        every { userService.createUser(any()) } returns Failure(CreateUserResultStatus.INVALID_EMAIL)

        val expected = ResponseEntity(
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
        val actual = userController.createUser(stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a user THEN 200`() {
        val expected = stubbedUser.toAPIDocument().toResponseEntity()
        val actual = userController.updateUser(stubbedUserId, stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a user NOT found THEN 404`() {
        every { userRepository.findByIdOrNull(any()) } returns null
        every { userService.updateUser(any(), any()) } returns Failure(CreateUserResultStatus.NOT_FOUND)

        val expected = ResponseEntity(
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
        val actual = userController.updateUser(stubbedUserId, stubbedCreateUserRequest)
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `WHEN updating a user with conflicting email THEN 409`() {
        every { userService.updateUser(any(), any()) } returns Failure(CreateUserResultStatus.EMAIL_CONFLICT)

        val expected = ResponseEntity(
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
        val actual = userController.updateUser(stubbedUserId, stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a user with bad email THEN 400`() {
        every { userService.updateUser(any(), any()) } returns Failure(CreateUserResultStatus.INVALID_EMAIL)

        val expected = ResponseEntity(
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
        val actual = userController.updateUser(stubbedUserId, stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }
}
