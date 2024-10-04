package com.fut.app.repository

import com.fut.app.entity.ComplexLocationEntity
import org.springframework.data.repository.CrudRepository

interface ComplexLocationRepository : CrudRepository<ComplexLocationEntity, Long> {

    fun existsByLatitudeAndLongitude(latitude: Double, longitude: Double): Boolean
}