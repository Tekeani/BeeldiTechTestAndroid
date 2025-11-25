package com.example.beelditechtest.presentation.equipment.list

import com.example.beelditechtest.domain.model.Equipment
import com.example.beelditechtest.domain.model.UserRole
import com.example.beelditechtest.domain.usecase.GetEquipmentsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class EquipmentListViewModelTest {
    private lateinit var viewModel: EquipmentListViewModel
    private lateinit var getEquipmentsUseCase: GetEquipmentsUseCase
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setup() {
        // Configurer le Main dispatcher pour les tests
        Dispatchers.setMain(testDispatcher)
        getEquipmentsUseCase = mock()
    }

    @After
    fun tearDown() {
        // Nettoyer le Main dispatcher après les tests
        Dispatchers.resetMain()
    }

    @Test
    fun `loadEquipments should emit Success state with all equipments for ADMIN role`() =
        testScope.runTest {
            // Given
            val equipments = createTestEquipments()
            whenever(getEquipmentsUseCase()).thenReturn(flowOf(Result.success(equipments)))

            viewModel = EquipmentListViewModel(getEquipmentsUseCase)

            // Attendre que le Flow soit collecté et que l'état soit mis à jour
            advanceUntilIdle()

            // When - Attendre que l'état ne soit plus Loading
            val state = viewModel.state.first { it !is EquipmentListUiState.Loading }

            // Then
            assertTrue("State should be Success but was $state", state is EquipmentListUiState.Success)
            val successState = state as EquipmentListUiState.Success
            assertEquals(3, successState.equipments.size)
            assertEquals(UserRole.ADMIN, successState.selectedRole)
        }

    @Test
    fun `selectRole MAINTAINER should filter equipments with type 0 and 1`() =
        testScope.runTest {
            // Given
            val equipments = createTestEquipments()
            whenever(getEquipmentsUseCase()).thenReturn(flowOf(Result.success(equipments)))

            viewModel = EquipmentListViewModel(getEquipmentsUseCase)
            advanceUntilIdle()

            // Attendre que l'état initial soit chargé
            viewModel.state.first { it !is EquipmentListUiState.Loading }

            // When
            viewModel.selectRole(UserRole.MAINTAINER)
            advanceUntilIdle()

            // Then
            val state = viewModel.state.value
            assertTrue("State should be Success but was $state", state is EquipmentListUiState.Success)
            val successState = state as EquipmentListUiState.Success
            assertEquals(2, successState.equipments.size) // Seulement type 0 et 1
            assertEquals(UserRole.MAINTAINER, successState.selectedRole)
            assertTrue(successState.equipments.all { it.type == 0 || it.type == 1 })
        }

    @Test
    fun `selectRole AUDITOR should filter equipments with type 0 only`() =
        testScope.runTest {
            // Given
            val equipments = createTestEquipments()
            whenever(getEquipmentsUseCase()).thenReturn(flowOf(Result.success(equipments)))

            viewModel = EquipmentListViewModel(getEquipmentsUseCase)
            advanceUntilIdle()

            // Attendre que l'état initial soit chargé
            viewModel.state.first { it !is EquipmentListUiState.Loading }

            // When
            viewModel.selectRole(UserRole.AUDITOR)
            advanceUntilIdle()

            // Then
            val state = viewModel.state.value
            assertTrue("State should be Success but was $state", state is EquipmentListUiState.Success)
            val successState = state as EquipmentListUiState.Success
            assertEquals(1, successState.equipments.size) // Seulement type 0
            assertEquals(UserRole.AUDITOR, successState.selectedRole)
            assertTrue(successState.equipments.all { it.type == 0 })
        }

    @Test
    fun `loadEquipments should emit Error state when use case fails`() =
        testScope.runTest {
            // Given
            val errorMessage = "Erreur de chargement"
            whenever(getEquipmentsUseCase()).thenReturn(
                flowOf(Result.failure(Exception(errorMessage))),
            )

            viewModel = EquipmentListViewModel(getEquipmentsUseCase)
            advanceUntilIdle()

            // When - Attendre que l'état ne soit plus Loading
            val state = viewModel.state.first { it !is EquipmentListUiState.Loading }

            // Then
            assertTrue("State should be Error but was $state", state is EquipmentListUiState.Error)
            val errorState = state as EquipmentListUiState.Error
            assertEquals(errorMessage, errorState.message)
        }

    private fun createTestEquipments(): List<Equipment> =
        listOf(
            Equipment(
                id = "1",
                name = "Équipement 1",
                brand = "Brand A",
                model = "Model X",
                serialNumber = "SN001",
                location = "Location 1",
                type = 0,
            ),
            Equipment(
                id = "2",
                name = "Équipement 2",
                brand = "Brand B",
                model = "Model Y",
                serialNumber = "SN002",
                location = "Location 2",
                type = 1,
            ),
            Equipment(
                id = "3",
                name = "Équipement 3",
                brand = "Brand C",
                model = "Model Z",
                serialNumber = "SN003",
                location = "Location 3",
                type = 2,
            ),
        )
}
