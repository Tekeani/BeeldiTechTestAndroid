package com.example.beelditechtest

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class EquipmentListViewModel(
    private val dataSource: EquipmentDataSource
) : ViewModel() {

    private val _state = MutableStateFlow(EquipmentListState())
    val state: StateFlow<EquipmentListState> = _state

    init {
        loadEquipments()
    }

    fun loadEquipments() {
        // TODO
    }
}
