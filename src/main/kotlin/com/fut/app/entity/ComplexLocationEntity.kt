package com.fut.app.entity

import com.fut.app.model.ComplexLocation
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "complex_locations")
data class ComplexLocationEntity(

    @Id
    @GeneratedValue
    val id: Long = 0,

    val latitude: Double = 0.0,

    val longitude: Double = 0.0
) {

    companion object {

        fun emptyLocation() = ComplexLocationEntity()
    }
}

fun ComplexLocationEntity.toComplexLocation() = ComplexLocation(
    id = id,
    latitude = latitude,
    longitude = longitude
)
