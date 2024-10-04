package com.fut.app.utils.complex

import com.fut.app.common.api.request.complex.CreateComplexLocationRequest
import com.fut.app.common.api.request.complex.CreateComplexRequest
import com.fut.app.common.api.request.complex.CreateFieldRequest
import com.fut.app.entity.ComplexEntity
import com.fut.app.entity.ComplexLocationEntity
import com.fut.app.entity.FieldEntity
import com.fut.app.utils.randomDoubleNonNegative
import com.fut.app.utils.randomIntNonNegative
import com.fut.app.utils.randomLongNonNegative
import com.fut.app.utils.randomString

fun stubbedComplexEntity(
    id: Long = randomLongNonNegative(),
    name: String = randomString(),
    address: String = randomString(),
    location: ComplexLocationEntity = stubbedComplexLocationEntity(),
    fields: List<FieldEntity> = listOf(stubbedFieldEntity())
) = ComplexEntity(
    id = id,
    name = name,
    address = address,
    location = location,
    fields = fields
)

fun stubbedComplexLocationEntity(
    latitude: Double = randomDoubleNonNegative(),
    longitude: Double = randomDoubleNonNegative()
) = ComplexLocationEntity(
    latitude = latitude,
    longitude = longitude
)

fun stubbedFieldEntity(
    id: Long = randomLongNonNegative(),
    name: String = randomString(),
    maxCapacity: Int = randomIntNonNegative(22)
) = FieldEntity(
    id = id,
    name = name,
    maxCapacity = maxCapacity
)

fun stubbedCreateComplexRequest(
    name: String = randomString(),
    address: String = randomString(),
    location: CreateComplexLocationRequest = stubbedCreateComplexLocationRequest(),
    fields: List<CreateFieldRequest> = listOf(stubbedCreateFieldRequest())
) = CreateComplexRequest(
    name = name,
    address = address,
    location = location,
    fields = fields
)

fun stubbedCreateComplexLocationRequest(
    latitude: Double = randomDoubleNonNegative(),
    longitude: Double = randomDoubleNonNegative()
) = CreateComplexLocationRequest(
    latitude = latitude,
    longitude = longitude
)

fun stubbedCreateFieldRequest(
    name: String = randomString(),
    maxCapacity: Int = randomIntNonNegative()
) = CreateFieldRequest(
    name = name,
    maxCapacity = maxCapacity
)
