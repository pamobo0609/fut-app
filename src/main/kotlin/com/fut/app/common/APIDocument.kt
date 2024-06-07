package com.fut.app.common

import com.fut.app.common.api.APIError
import com.fut.app.common.api.APIMessage
import com.fut.app.common.api.APIResult.Success
import com.fut.app.common.api.APIResult.Failure
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class APIDocument<T>(
    @Schema(required = false, description = "The document's primary data.")
    val data: T? = null,

    @Schema(required = true, description = "An array of API Messages.")
    val messages: List<APIMessage> = emptyList(),

    @Schema(required = true, description = "An array of error objects.")
    val errors: List<APIError> = emptyList()
) {

    fun toResponseEntity(status: HttpStatus = HttpStatus.OK) = ResponseEntity(this, status)
}

fun <T> T.toAPIDocument(messages: List<APIMessage> = emptyList()): APIDocument<T> = APIDocument(this, messages)

fun <V> Success<V>.toApiDocument(messages: List<APIMessage> = emptyList()): APIDocument<V> = APIDocument(this.value, messages)
fun <V> Success<V>.toApiResponseEntity(status: HttpStatus = HttpStatus.OK, messages: List<APIMessage> = emptyList()) = ResponseEntity<APIDocument<V>>(APIDocument(this.value, messages), status)

fun Failure<APIError>.toAPIDocument(messages: List<APIMessage> = emptyList()): APIDocument<Nothing> = APIDocument(null, messages, listOf(this.error))

fun <T> APIError.toApiDocument() = APIDocument<T>(null, emptyList(), listOf(this))

fun <T> List<APIError>.toAPIDocument(messages: List<APIMessage> = emptyList())= APIDocument<T>(null, emptyList(), this)