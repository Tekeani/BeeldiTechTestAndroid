package com.example.beelditechtest.data.repository
import com.example.beelditechtest.data.datasource.local.EquipmentLocalDataSource
import com.example.beelditechtest.data.mapper.EquipmentMapper
import com.example.beelditechtest.domain.model.Equipment
import com.example.beelditechtest.domain.repository.EquipmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class EquipmentRepositoryImpl(
    private val localDataSource: EquipmentLocalDataSource,
) : EquipmentRepository {
    override fun getEquipments(): Flow<Result<List<Equipment>>> =
        localDataSource
            .getEquipments()
            .map { entities ->
                val equipments = EquipmentMapper.toDomainList(entities)
                Result.success(equipments)
            }.catch { e ->
                emit(Result.failure(e))
            }
}
