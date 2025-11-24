package com.example.beelditechtest.presentation.equipment.detail

import com.example.beelditechtest.domain.model.Equipment
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EquipmentDetailViewModelTest {

    @Test
    fun `should initialize with equipment data and emit Success state`() {
        // Given
        val equipment = Equipment(
            id = "1",
            name = "Équipement Test",
            brand = "Brand A",
            model = "Model X",
            serialNumber = "SN001",
            location = "Location 1",
            type = 0
        )

        // When
        val viewModel = EquipmentDetailViewModel(equipment)

        // Then
        val state = viewModel.state.value
        assertTrue("State should be Success", state is EquipmentDetailUiState.Success)
        val successState = state as EquipmentDetailUiState.Success
        assertEquals(equipment, successState.equipment)
        assertEquals("Équipement Test", successState.equipment.name)
        assertEquals("Brand A", successState.equipment.brand)
        assertEquals("Model X", successState.equipment.model)
        assertEquals("SN001", successState.equipment.serialNumber)
        assertEquals("Location 1", successState.equipment.location)
        assertEquals(0, successState.equipment.type)
    }
}

