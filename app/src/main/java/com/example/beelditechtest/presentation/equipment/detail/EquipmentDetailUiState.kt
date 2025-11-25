package com.example.beelditechtest.presentation.equipment.detail
import com.example.beelditechtest.domain.model.Equipment

sealed class EquipmentDetailUiState {
    data object Loading : EquipmentDetailUiState()

    data class Success(
        val equipment: Equipment,
    ) : EquipmentDetailUiState()

    data class Error(
        val message: String,
    ) : EquipmentDetailUiState()
}
