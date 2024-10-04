package com.fut.app.common.api.request.complex

import com.fut.app.entity.FieldEntity
import io.swagger.v3.oas.annotations.media.Schema

data class CreateFieldRequest(
    @Schema(description = "The preferred name for this field", examples = ["Soccer Field 1", "My Cleats 5"])
    val name: String,

    @Schema(description = "Maximum capability of each field", examples = ["10", "12", "14", "18", "22"])
    val maxCapacity: Int
)

fun CreateFieldRequest.toEntity() = FieldEntity(
    name = name,
    maxCapacity = maxCapacity
)
