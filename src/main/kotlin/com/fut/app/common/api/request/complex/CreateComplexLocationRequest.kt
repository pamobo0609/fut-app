package com.fut.app.common.api.request.complex

import com.fut.app.entity.ComplexLocationEntity
import io.swagger.v3.oas.annotations.media.Schema

data class CreateComplexLocationRequest(

    @Schema(description = "The complex latitude", required = true)
    val latitude: Double,

    @Schema(description = "The complex longitude", required = true)
    val longitude: Double
)

fun CreateComplexLocationRequest.toEntity() = ComplexLocationEntity(
    latitude = latitude,
    longitude = longitude
)
