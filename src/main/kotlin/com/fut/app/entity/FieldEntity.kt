package com.fut.app.entity

import com.fut.app.common.EMPTY_STRING
import com.fut.app.model.Field
import jakarta.persistence.*

@Entity
@Table(name = "fields")
data class FieldEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val name: String = EMPTY_STRING,

    val maxCapacity: Int = 0
)

fun FieldEntity.toField() = Field(
    id = id,
    name = name,
    maxCapacity = maxCapacity
)
