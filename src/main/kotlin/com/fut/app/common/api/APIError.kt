package com.fut.app.common.api

import io.swagger.v3.oas.annotations.media.Schema
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

data class APIError(
    @Schema(required = true, description = "The actual API error code. HTTP related.")
    val code: Int,

    @Schema(required = true, description = "A short title for the error.")
    val title: String,

    @Schema(required = true, description = "An explanation for the error.")
    val detail: String
) {

    fun logError(errorMessage: String) = logger.error("$code - $errorMessage")

    companion object {
        val logger: Logger = LoggerFactory.getLogger(APIError::class.java)
    }
}