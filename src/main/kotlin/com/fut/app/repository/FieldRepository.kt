package com.fut.app.repository

import com.fut.app.entity.FieldEntity
import org.springframework.data.repository.CrudRepository

interface FieldRepository: CrudRepository<FieldEntity, Long>
