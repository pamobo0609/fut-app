package com.fut.app.model

import com.fut.app.entity.ComplexLocationEntity

data class ComplexLocation(
    val id: Long,
    val latitude: Double,
    val longitude: Double
)

fun ComplexLocation.toEntity() = ComplexLocationEntity(
    id = id,
    latitude = latitude,
    longitude = longitude
)
