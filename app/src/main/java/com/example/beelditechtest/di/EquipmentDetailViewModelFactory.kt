package com.example.beelditechtest.di
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beelditechtest.domain.model.Equipment
import com.example.beelditechtest.presentation.equipment.detail.EquipmentDetailViewModel

class EquipmentDetailViewModelFactory(
    private val equipment: Equipment,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EquipmentDetailViewModel::class.java)) {
            return EquipmentDetailViewModel(equipment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
