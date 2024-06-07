package com.fut.app.common.api

import io.swagger.v3.oas.annotations.media.Schema

data class APIMessage(
    @Schema(required = true, description = "The API response code.")
    val code: Int,

    @Schema(required = true, description = "A detail explaining the code.")
    val detail: String
)