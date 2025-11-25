package com.example.beelditechtest.presentation.equipment.list
import com.example.beelditechtest.domain.model.Equipment
import com.example.beelditechtest.domain.model.UserRole

sealed class EquipmentListUiState {
    data object Loading : EquipmentListUiState()

    data class Success(
        val equipments: List<Equipment>,
        val selectedRole: UserRole = UserRole.ADMIN,
    ) : EquipmentListUiState()

    data class Error(
        val message: String,
    ) : EquipmentListUiState()
}
