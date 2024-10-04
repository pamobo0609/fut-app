package com.fut.app.service

import com.fut.app.common.api.APIResult.Failure
import com.fut.app.common.api.APIResult.Success
import com.fut.app.common.api.request.complex.CreateComplexLocationRequest
import com.fut.app.common.api.request.complex.CreateComplexRequest
import com.fut.app.common.api.request.complex.CreateComplexRequestResultStatus
import com.fut.app.common.api.request.complex.toEntity
import com.fut.app.entity.ComplexEntity
import com.fut.app.entity.toComplex
import com.fut.app.repository.ComplexLocationRepository
import com.fut.app.repository.ComplexRepository
import com.fut.app.service.complex.ComplexService
import com.fut.app.utils.complex.stubbedComplexEntity
import com.fut.app.utils.complex.stubbedCreateComplexRequest
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.repository.findByIdOrNull
import kotlin.test.Ignore

class ComplexServiceTest {

    private val complexRepository: ComplexRepository = mockk()
    private val complexLocationRepository: ComplexLocationRepository = mockk()

    private val complexService: ComplexService = ComplexService(complexRepository, complexLocationRepository)

    private val stubbedComplexEntity: ComplexEntity = stubbedComplexEntity()
    private val stubbedCreateComplexRequest: CreateComplexRequest = stubbedCreateComplexRequest()

    @BeforeEach
    fun before() {
        every { complexRepository.findAll() } returns listOf(stubbedComplexEntity)
        every { complexRepository.findByIdOrNull(any()) } returns stubbedComplexEntity
        every { complexRepository.existsByName(any()) } returns false
        every { complexLocationRepository.existsByLatitudeAndLongitude(any(), any()) } returns false
        every { complexRepository.save(any()) } returns stubbedCreateComplexRequest.toEntity()
        every { complexRepository.delete(any()) } just runs
    }

    @Test
    fun `WHEN getting all complexes THEN return the available complexes`() {
        val expected = listOf(stubbedComplexEntity.toComplex())
        val actual = complexService.getAllComplexes()
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN getting all complexes AND NO complexes THEN return an empty list`() {
        every { complexRepository.findAll() } returns emptyList()

        val actual = complexService.getAllComplexes()
        assertThat(actual).isEqualTo(emptyList<ComplexEntity>())
    }

    @Test
    fun `WHEN creating a complex THEN return the newly created complex`() {
        every { complexRepository.existsByName(any()) } returns false

        val expected = Success(stubbedCreateComplexRequest.toEntity())
        val actual = complexService.createComplex(stubbedCreateComplexRequest)
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `WHEN creating a complex AND the request has an existing name THEN return Failure`() {
        every { complexRepository.existsByName(any()) } returns true

        val expected = Failure(CreateComplexRequestResultStatus.NAME_CONFLICT)
        val actual = complexService.createComplex(stubbedCreateComplexRequest)
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `WHEN creating a complex AND the request has an existing location THEN return Failure`() {
        every { complexLocationRepository.existsByLatitudeAndLongitude(any(), any()) } returns true

        val expected = Failure(CreateComplexRequestResultStatus.LOCATION_CONFLICT)
        val actual = complexService.createComplex(stubbedCreateComplexRequest)
        assertThat(expected).isEqualTo(actual)
    }

    @Test
    fun `WHEN creating a complex AND unexpected error THEN throw the error`() {
        every { complexRepository.save(any()) } throws RuntimeException("error")

        assertThrows<Exception> { complexService.createComplex(stubbedCreateComplexRequest) }
    }

    @Ignore("Handled in a functional test")
    @Test
    fun `WHEN updating a user and the id is invalid THEN return Failure`() {
        // functional test
    }

    @Test
    fun `WHEN updating a complex THEN return the updated complex`() {
        val newName = "Changed"
        val modifiedEntity = stubbedComplexEntity.copy(name = newName)

        every { complexRepository.save(any()) } returns modifiedEntity

        val expected = Success(modifiedEntity.toComplex())
        val actual = complexService.updateComplex(
            id = stubbedComplexEntity.id,
            request = stubbedCreateComplexRequest(
                name = newName,
                address = stubbedComplexEntity.address,
                location = CreateComplexLocationRequest(
                    stubbedCreateComplexRequest.location.latitude,
                    stubbedCreateComplexRequest.location.longitude
                ),
                fields = stubbedCreateComplexRequest.fields ?: emptyList()
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a complex AND the complex is not found THEN return Failure`() {
        every { complexRepository.findByIdOrNull(any()) } returns null
        val expected = Failure(CreateComplexRequestResultStatus.NOT_FOUND)
        val actual = complexService.updateComplex(
            stubbedComplexEntity.id,
            request = stubbedCreateComplexRequest(
                name = "Changed",
                address = stubbedComplexEntity.address,
                location = CreateComplexLocationRequest(
                    stubbedCreateComplexRequest.location.latitude,
                    stubbedCreateComplexRequest.location.longitude
                ),
                fields = stubbedCreateComplexRequest.fields ?: emptyList()
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a complex AND the new name is conflicting THEN return Failure`() {
        every { complexRepository.existsByName(any()) } returns true
        val expected = Failure(CreateComplexRequestResultStatus.NAME_CONFLICT)
        val actual = complexService.updateComplex(
            stubbedComplexEntity.id,
            request = stubbedCreateComplexRequest(
                name = stubbedComplexEntity.name,
                address = stubbedComplexEntity.address,
                location = CreateComplexLocationRequest(
                    stubbedCreateComplexRequest.location.latitude,
                    stubbedCreateComplexRequest.location.longitude
                ),
                fields = stubbedCreateComplexRequest.fields ?: emptyList()
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a complex AND the location is conflicting THEN return Failure`() {
        every { complexLocationRepository.existsByLatitudeAndLongitude(any(), any()) } returns true
        val expected = Failure(CreateComplexRequestResultStatus.LOCATION_CONFLICT)
        val actual = complexService.updateComplex(
            stubbedComplexEntity.id,
            request = stubbedCreateComplexRequest(
                name = "Changed",
                address = stubbedComplexEntity.address,
                location = CreateComplexLocationRequest(
                    stubbedCreateComplexRequest.location.latitude,
                    stubbedCreateComplexRequest.location.longitude
                ),
                fields = stubbedCreateComplexRequest.fields ?: emptyList()
            )
        )
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN updating a complex AND unexpected error THEN throw the error`() {
        every { complexRepository.save(any()) } throws RuntimeException("error")

        assertThrows<Exception> { complexService.updateComplex(stubbedComplexEntity.id, stubbedCreateComplexRequest) }
    }

    @Test
    fun `WHEN deleting a complex then return true`() {
        val actual = complexService.deleteComplex(stubbedComplexEntity.id)
        assertThat(actual).isEqualTo(Success(true))
    }

    @Test
    fun `WHEN deleting a complex with a non existent id THEN return Failure`() {
        every { complexRepository.findByIdOrNull(any()) } returns null

        val expected = Failure(CreateComplexRequestResultStatus.NOT_FOUND)
        val actual = complexService.deleteComplex(stubbedComplexEntity.id)
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `WHEN deleting a complex AND unexpected error THEN throw the error`() {
        every { complexRepository.delete(any()) } throws RuntimeException("error")

        assertThrows<Exception> { complexService.deleteComplex(stubbedComplexEntity.id) }
    }
}
