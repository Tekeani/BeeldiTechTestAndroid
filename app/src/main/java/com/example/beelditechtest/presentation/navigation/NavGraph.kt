package com.example.beelditechtest.presentation.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.beelditechtest.di.EquipmentDetailViewModelFactory
import com.example.beelditechtest.domain.model.Equipment
import com.example.beelditechtest.presentation.equipment.detail.EquipmentDetailScreen
import com.example.beelditechtest.presentation.equipment.detail.EquipmentDetailViewModel
import com.example.beelditechtest.presentation.equipment.list.EquipmentListScreen
import com.example.beelditechtest.presentation.equipment.list.EquipmentListUiState
import com.example.beelditechtest.presentation.equipment.list.EquipmentListViewModel
import kotlinx.coroutines.flow.first

sealed class Screen(
    val route: String,
) {
    object EquipmentList : Screen("equipment_list")

    object EquipmentDetail : Screen("equipment_detail/{equipmentId}") {
        fun createRoute(equipmentId: String) = "equipment_detail/$equipmentId"
    }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    equipmentListViewModel: EquipmentListViewModel,
    equipmentDetailViewModelFactory: (Equipment) -> EquipmentDetailViewModelFactory,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.EquipmentList.route,
    ) {
        composable(Screen.EquipmentList.route) {
            val uiState by equipmentListViewModel.state.collectAsState()
            EquipmentListScreen(
                uiState = uiState,
                onRetry = { equipmentListViewModel.loadEquipments() },
                onRoleSelected = { role ->
                    equipmentListViewModel.selectRole(role)
                },
                onEquipmentClick = { equipment ->
                    navController.navigate(
                        Screen.EquipmentDetail.createRoute(equipment.id),
                    )
                },
            )
        }

        composable(
            route = Screen.EquipmentDetail.route,
            arguments =
                listOf(
                    navArgument("equipmentId") {
                        type = NavType.StringType
                    },
                ),
        ) { backStackEntry ->
            val equipmentId = backStackEntry.arguments?.getString("equipmentId") ?: ""
            var equipment by remember(equipmentId) { mutableStateOf<Equipment?>(null) }

            LaunchedEffect(equipmentId) {
                val state =
                    equipmentListViewModel.state.first {
                        it is EquipmentListUiState.Success || it is EquipmentListUiState.Error
                    }
                equipment =
                    when (state) {
                        is EquipmentListUiState.Success -> {
                            state.equipments.find { it.id == equipmentId }
                        }
                        else -> null
                    }
            }

            equipment?.let { eq ->
                val detailViewModel: EquipmentDetailViewModel =
                    viewModel(
                        key = "equipment_detail_$equipmentId",
                        factory = equipmentDetailViewModelFactory(eq),
                    )
                val detailUiState by detailViewModel.state.collectAsState()

                EquipmentDetailScreen(
                    uiState = detailUiState,
                    onBack = { navController.popBackStack() },
                )
            }
        }
    }
}
