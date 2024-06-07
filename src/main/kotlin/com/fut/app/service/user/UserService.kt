package com.fut.app.service.user

import com.fut.app.common.EMAIL_REGEX_PATTERN
import com.fut.app.common.LogDiagnostics
import com.fut.app.common.api.APIResult
import com.fut.app.common.api.APIResult.Failure
import com.fut.app.common.api.APIResult.Success
import com.fut.app.common.api.request.CreateUserRequest
import com.fut.app.common.api.request.CreateUserResultStatus
import com.fut.app.common.api.request.toEntity
import com.fut.app.entity.UserEntity
import com.fut.app.entity.toUser
import com.fut.app.model.User
import com.fut.app.repository.UserRepository
import com.fut.app.service.user.GetAllUsersFailure.UsersNotFound
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
@LogDiagnostics
class UserService(
    private val userRepository: UserRepository
) {

    fun getAllUsers(): APIResult<List<User>, GetAllUsersFailure> {
        val users = userRepository.findAll().toList()
        return if (users.isNotEmpty()) Success(users.map { it.toUser() }) else Failure(UsersNotFound)
    }

    fun createUser(request: CreateUserRequest): APIResult<User, CreateUserResultStatus> = when {
        // Conflict avoidance
        userRepository.existsByEmail(request.email) -> Failure(CreateUserResultStatus.EMAIL_CONFLICT)
        // Format validation
        isEmailValid(request.email) -> Failure(CreateUserResultStatus.INVALID_EMAIL)
        // Otherwise, the user will be added
        else -> Success(saveUser(request.toEntity()).toUser())
    }

    fun updateUser(
        id: Long,
        request: CreateUserRequest
    ): APIResult<User, CreateUserResultStatus> =
        when (val user = userRepository.findByIdOrNull(id)) {
            null -> Failure(CreateUserResultStatus.NOT_FOUND)
            else -> updateUser(user, request)
        }

    private fun isEmailValid(email: String) = email.isNotBlank() && Pattern.matches(EMAIL_REGEX_PATTERN, email)

    private fun saveUser(userEntity: UserEntity) = try {
        userRepository.save(userEntity)
    } catch (ex: Exception) {
        logger.error("Failed to save user", ex)
        throw ex
    }

    private fun updateUser(
        userEntity: UserEntity,
        updateRequest: CreateUserRequest
    ): APIResult<User, CreateUserResultStatus> {
        when {
            // Conflict avoidance
            userRepository.existsByEmail(updateRequest.email) -> return Failure(CreateUserResultStatus.EMAIL_CONFLICT)
            // Format validation
            isEmailValid(updateRequest.email) -> return Failure(CreateUserResultStatus.INVALID_EMAIL)
        }

        try {
            val userToUpdate = userEntity.copy(
                name = updateRequest.name,
                lastName = updateRequest.lastName,
                email = updateRequest.email
            )
            return Success(userRepository.save(userToUpdate).toUser())
        } catch (ex: Exception) {
            logger.error("Failed to update user", ex)
            throw ex
        }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
    }
}

sealed class GetAllUsersFailure {
    data object UsersNotFound : GetAllUsersFailure()
}
