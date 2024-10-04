package com.fut.app.common.api.request.complex

import com.fut.app.entity.ComplexEntity
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Request for creating a complex")
data class CreateComplexRequest(
    @Schema(examples = ["My Complex", "Awesometacular Soccer", "Soccer Greatness Fields"], required = true)
    val name: String,

    @Schema(examples = ["P. Sherman 42 Wallaby Way, Sydney"], required = true)
    val address: String,

    @Schema(example = "-0.3131508,-108.368608", required = true)
    val location: CreateComplexLocationRequest,

    @Schema(description = "The list of soccer fields associated with the complex", required = false)
    val fields: List<CreateFieldRequest>?
)

fun CreateComplexRequest.toEntity() = ComplexEntity(
    name = name,
    address = address,
    location = location.toEntity(),
    fields = fields?.map { it.toEntity() } ?: emptyList()
)

enum class CreateComplexRequestResultStatus {
    CREATED,
    NAME_CONFLICT,
    LOCATION_CONFLICT,
    NOT_FOUND
}
