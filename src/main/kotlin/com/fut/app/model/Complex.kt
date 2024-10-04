package com.fut.app.model

import com.fut.app.entity.ComplexEntity

data class Complex(
    val id: Long,
    val name: String,
    val address: String,
    val location: ComplexLocation,
    val fields: List<Field>
)

fun Complex.toEntity() = ComplexEntity(
    id = id,
    name = name,
    address = address,
    location = location.toEntity(),
    fields = fields.map { it.toEntity() }
)
