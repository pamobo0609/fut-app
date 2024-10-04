package com.fut.app.repository

import com.fut.app.entity.ComplexEntity
import org.springframework.data.repository.CrudRepository

interface ComplexRepository : CrudRepository<ComplexEntity, Long> {

    fun existsByName(name: String): Boolean
}
