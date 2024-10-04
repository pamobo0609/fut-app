package com.fut.app.model

import com.fut.app.entity.FieldEntity

data class Field(
    val id: Long,
    val name: String,
    val maxCapacity: Int
)

fun Field.toEntity() = FieldEntity(
    id = id,
    name = name,
    maxCapacity = maxCapacity
)