package com.fut.app.service.complex

import com.fut.app.common.LogDiagnostics
import com.fut.app.common.api.APIResult
import com.fut.app.common.api.request.complex.CreateComplexRequest
import com.fut.app.entity.toComplex
import com.fut.app.repository.ComplexRepository
import org.springframework.stereotype.Service
import com.fut.app.common.api.APIResult.Failure
import com.fut.app.common.api.APIResult.Success
import com.fut.app.common.api.request.complex.CreateComplexRequestResultStatus
import com.fut.app.common.api.request.complex.toEntity
import com.fut.app.entity.ComplexEntity
import com.fut.app.model.Complex
import com.fut.app.repository.ComplexLocationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull

@Service
@LogDiagnostics
class ComplexService(
    private val complexRepository: ComplexRepository,
    private val complexLocationRepository: ComplexLocationRepository
) {

    fun getAllComplexes() = complexRepository.findAll().toList().map { it.toComplex() }

    fun createComplex(request: CreateComplexRequest) = when {
        // Name conflict
        complexRepository.existsByName(request.name) -> Failure(CreateComplexRequestResultStatus.NAME_CONFLICT)
        // Location conflict
        complexLocationRepository.existsByLatitudeAndLongitude(request.location.latitude, request.location.longitude) -> Failure(CreateComplexRequestResultStatus.LOCATION_CONFLICT)
        // Nothing wrong, proceed to save
        else -> Success(saveComplex(request.toEntity()))
    }

    fun updateComplex(id: Long, request: CreateComplexRequest) = when(val complex = complexRepository.findByIdOrNull(id)) {
        null -> Failure(CreateComplexRequestResultStatus.NOT_FOUND)
        else -> updateComplex(complex, request)
    }

    fun deleteComplex(id: Long) = when (val complex = complexRepository.findByIdOrNull(id)) {
        null -> Failure(CreateComplexRequestResultStatus.NOT_FOUND)
        else -> Success(deleteComplex(complex))
    }

    private fun saveComplex(complexEntity: ComplexEntity) = try {
        complexRepository.save(complexEntity)
    } catch (ex: Exception) {
        logger.error("Failed to save complex", ex)
        throw ex
    }

    private fun updateComplex(complexEntity: ComplexEntity, request: CreateComplexRequest): APIResult<Complex, CreateComplexRequestResultStatus> {
        when {
            // Name conflict
            complexRepository.existsByName(request.name) -> return Failure(CreateComplexRequestResultStatus.NAME_CONFLICT)
            // Location conflict
            complexLocationRepository.existsByLatitudeAndLongitude(
                request.location.latitude,
                request.location.longitude
            ) -> return Failure(CreateComplexRequestResultStatus.LOCATION_CONFLICT)
        }

        try {
            val complexToUpdate = complexEntity.copy(
                name = request.name,
                address = request.address,
                location = request.location.toEntity(),
                fields = request.fields?.map { it.toEntity() } ?: emptyList()
            )

            return Success(complexRepository.save(complexToUpdate).toComplex())
        } catch (ex: Exception) {
            logger.error("Failed to update complex", ex)
            throw ex
        }
    }

    private fun deleteComplex(complex: ComplexEntity): Boolean = try {
        complexRepository.delete(complex)
        true
    } catch (ex: Exception) {
        logger.error("Failed to delete complex", ex)
        throw ex
    }

    companion object {

        private val logger: Logger = LoggerFactory.getLogger(ComplexService::class.java)
    }
}
