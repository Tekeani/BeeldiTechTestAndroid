package com.example.beelditechtest.presentation.equipment.detail
import androidx.lifecycle.ViewModel
import com.example.beelditechtest.domain.model.Equipment
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EquipmentDetailViewModel(
    equipment: Equipment,
) : ViewModel() {
    private val _state =
        MutableStateFlow<EquipmentDetailUiState>(
            EquipmentDetailUiState.Success(equipment),
        )
    val state: StateFlow<EquipmentDetailUiState> = _state.asStateFlow()
}
