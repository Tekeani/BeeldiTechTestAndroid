package com.example.beelditechtest.presentation.equipment.list
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beelditechtest.domain.model.Equipment
import com.example.beelditechtest.domain.model.UserRole
import com.example.beelditechtest.domain.usecase.GetEquipmentsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EquipmentListViewModel(
    private val getEquipmentsUseCase: GetEquipmentsUseCase,
) : ViewModel() {
    private val _allEquipments = MutableStateFlow<List<Equipment>>(emptyList())
    private val _selectedRole = MutableStateFlow<UserRole>(UserRole.ADMIN)
    private val _isLoading = MutableStateFlow<Boolean>(true)
    private val _error = MutableStateFlow<String?>(null)

    val state: StateFlow<EquipmentListUiState> =
        combine(
            _allEquipments,
            _selectedRole,
            _isLoading,
            _error,
        ) { allEquipments, selectedRole, isLoading, error ->
            when {
                isLoading -> EquipmentListUiState.Loading
                error != null -> EquipmentListUiState.Error(error)
                else -> {
                    val filteredEquipments = filterEquipmentsByRole(allEquipments, selectedRole)
                    EquipmentListUiState.Success(
                        equipments = filteredEquipments,
                        selectedRole = selectedRole,
                    )
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = EquipmentListUiState.Loading,
        )

    init {
        loadEquipments()
    }

    fun loadEquipments() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            getEquipmentsUseCase().collect { result ->
                if (result.isSuccess) {
                    _allEquipments.value = result.getOrNull() ?: emptyList()
                    _isLoading.value = false
                } else {
                    val exception = result.exceptionOrNull()
                    _error.value = exception?.message ?: "Erreur inconnue"
                    _isLoading.value = false
                }
            }
        }
    }

    fun selectRole(role: UserRole) {
        _selectedRole.value = role
    }

    private fun filterEquipmentsByRole(
        equipments: List<Equipment>,
        role: UserRole,
    ): List<Equipment> =
        when (role) {
            UserRole.ADMIN -> equipments
            UserRole.MAINTAINER -> equipments.filter { it.type == 0 || it.type == 1 }
            UserRole.AUDITOR -> equipments.filter { it.type == 0 }
        }
}
