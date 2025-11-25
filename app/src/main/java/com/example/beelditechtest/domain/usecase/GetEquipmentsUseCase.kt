package com.example.beelditechtest.domain.usecase
import com.example.beelditechtest.domain.model.Equipment
import com.example.beelditechtest.domain.repository.EquipmentRepository
import kotlinx.coroutines.flow.Flow

class GetEquipmentsUseCase(
    private val repository: EquipmentRepository,
) {
    operator fun invoke(): Flow<Result<List<Equipment>>> = repository.getEquipments()
}
