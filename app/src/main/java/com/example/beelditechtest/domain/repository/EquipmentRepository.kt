package com.example.beelditechtest.domain.repository
import com.example.beelditechtest.domain.model.Equipment
import kotlinx.coroutines.flow.Flow

interface EquipmentRepository {
    fun getEquipments(): Flow<Result<List<Equipment>>>
}
