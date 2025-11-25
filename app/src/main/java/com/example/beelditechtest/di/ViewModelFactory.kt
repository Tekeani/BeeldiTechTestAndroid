package com.example.beelditechtest.di
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beelditechtest.domain.usecase.GetEquipmentsUseCase
import com.example.beelditechtest.presentation.equipment.list.EquipmentListViewModel

class EquipmentViewModelFactory(
    private val getEquipmentsUseCase: GetEquipmentsUseCase,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EquipmentListViewModel::class.java)) {
            return EquipmentListViewModel(getEquipmentsUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
