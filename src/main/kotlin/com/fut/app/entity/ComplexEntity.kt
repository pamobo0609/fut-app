package com.fut.app.entity

import com.fut.app.common.EMPTY_STRING
import com.fut.app.model.Complex
import jakarta.persistence.*

@Entity
@Table(name = "complexes")
data class ComplexEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true)
    val name: String = EMPTY_STRING,

    val address: String = EMPTY_STRING,

    @Column(unique = true)
    @JoinColumn(name = "locationId", referencedColumnName = "id")
    val location: ComplexLocationEntity = ComplexLocationEntity.emptyLocation(),

    val fields: List<FieldEntity> = emptyList()
)

fun ComplexEntity.toComplex() = Complex(
    id = id,
    name = name,
    address = address,
    location = location.toComplexLocation(),
    fields = fields.map { it.toField() }
)
