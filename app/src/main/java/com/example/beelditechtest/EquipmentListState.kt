package com.example.beelditechtest

data class EquipmentListState(
    val equipmentEntities: List<EquipmentEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)