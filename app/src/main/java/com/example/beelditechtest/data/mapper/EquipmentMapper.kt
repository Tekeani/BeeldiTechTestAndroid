package com.example.beelditechtest.data.mapper
import com.example.beelditechtest.data.model.EquipmentEntity
import com.example.beelditechtest.domain.model.Equipment

object EquipmentMapper {
    fun toDomain(entity: EquipmentEntity): Equipment =
        Equipment(
            id = entity.id,
            name = entity.name,
            brand = entity.brand,
            model = entity.model,
            serialNumber = entity.serialNumber,
            location = entity.location,
            type = entity.type,
        )

    fun toDomainList(entities: List<EquipmentEntity>): List<Equipment> = entities.map { toDomain(it) }
}
