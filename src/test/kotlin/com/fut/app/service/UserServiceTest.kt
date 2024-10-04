package com.fut.app.service

import com.fut.app.common.api.APIResult.Failure
import com.fut.app.common.api.APIResult.Success
import com.fut.app.common.api.request.CreateUserResultStatus
import com.fut.app.common.api.request.toEntity
import com.fut.app.entity.UserEntity
import com.fut.app.entity.toUser
import com.fut.app.repository.UserRepository
import com.fut.app.service.user.UserService
import com.fut.app.utils.randomString
import com.fut.app.utils.user.stubbedCreateUserRequest
import com.fut.app.utils.user.stubbedUserEntity
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import java.util.*
import kotlin.test.Ignore

class UserServiceTest {

    private val userRepository: UserRepository = mockk()
    private val userService: UserService = UserService(userRepository)

    private val stubbedUserEntity = stubbedUserEntity()
    private val stubbedCreateUserRequest = stubbedCreateUserRequest()

    @BeforeEach
    fun before() {
        every { userRepository.findAll() } returns listOf(stubbedUserEntity)
        every { userRepository.save(any()) } returns stubbedCreateUserRequest.toEntity()
        every { userRepository.existsByEmail(any()) } returns false
        every { userRepository.findById(any()) } returns Optional.of(stubbedUserEntity)
        every { userRepository.findByIdOrNull(any()) } returns stubbedUserEntity
    }

    @Test
    fun `WHEN getting all users THEN return the available users`() {
        val expected = listOf(stubbedUserEntity.toUser())
        val actual = userService.getAllUsers()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN getting all users AND NO users THEN return an empty list`() {
        every { userRepository.findAll() } returns emptyList()

        val actual = userService.getAllUsers()
        assertThat(actual).isEqualTo(emptyList<UserEntity>())
    }

    @Test
    fun `WHEN creating a user THEN return the newly created user`() {
        val expected = Success(stubbedCreateUserRequest.toEntity().toUser())
        val actual = userService.createUser(stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN creating a user AND the request has a existing email THEN return Failure`() {
        every { userRepository.existsByEmail(any()) } returns true

        val expected = Failure(CreateUserResultStatus.EMAIL_CONFLICT)
        val actual = userService.createUser(stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN creating a user AND the email is invalid THEN return Failure`() {
        val expected = Failure(CreateUserResultStatus.INVALID_EMAIL)
        val actual = userService.createUser(stubbedCreateUserRequest.copy(email = randomString()))
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN creating a user AND unexpected error THEN throw the error`() {
        every { userRepository.save(any()) } throws RuntimeException("error")
        assertThrows<Exception> { userService.createUser(stubbedCreateUserRequest) }
    }

    @Test
    fun `WHEN updating a user THEN return the updated user`() {
        val newName = "Changed"
        val modifiedEntity = stubbedCreateUserRequest.toEntity().copy(name = newName)

        every { userRepository.save(any()) } returns modifiedEntity

        val expected = Success(modifiedEntity.toUser())
        val actual = userService.updateUser(
            id = stubbedUserEntity.id,
            request = stubbedCreateUserRequest(
                name = stubbedUserEntity.name,
                lastName = stubbedUserEntity.lastName,
                email = stubbedUserEntity.email,
                password = stubbedUserEntity.password
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a user AND the user is not found then return Failure`() {
        every { userRepository.findByIdOrNull(any()) } returns null
        val expected = Failure(CreateUserResultStatus.NOT_FOUND)
        val actual = userService.updateUser(stubbedUserEntity.id, stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }

    @Ignore("Handled in a functional test")
    @Test
    fun `WHEN updating a user and the id is invalid THEN return Failure`() {
        // functional test
    }

    @Test
    fun `WHEN updating a user and the request has a existing email THEN return Failure`() {
        every { userRepository.existsByEmail(any()) } returns true
        val expected = Failure(CreateUserResultStatus.EMAIL_CONFLICT)
        val actual = userService.updateUser(stubbedUserEntity.id, stubbedCreateUserRequest)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a user and the request has an invalid email THEN return Failure`() {
        val expected = Failure(CreateUserResultStatus.INVALID_EMAIL)
        val actual = userService.updateUser(stubbedUserEntity.id, stubbedCreateUserRequest.copy(email = randomString()))
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a user AND unexpected error THEN throw the error`() {
        every { userRepository.save(any()) } throws RuntimeException("error")

        assertThrows<Exception> { userService.updateUser(stubbedUserEntity.id, stubbedCreateUserRequest) }
    }
}
