package com.example.beelditechtest.presentation.main
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.example.beelditechtest.di.AppModule
import com.example.beelditechtest.di.EquipmentDetailViewModelFactory
import com.example.beelditechtest.di.EquipmentViewModelFactory
import com.example.beelditechtest.presentation.equipment.list.EquipmentListViewModel
import com.example.beelditechtest.presentation.navigation.AppNavGraph
import com.example.beelditechtest.presentation.theme.BeeldiTechTestTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<EquipmentListViewModel> {
        // Création de la chaîne de dépendances via AppModule
        val dataSource = AppModule.provideEquipmentLocalDataSource(this@MainActivity)
        val repository = AppModule.provideEquipmentRepository(dataSource)
        val useCase = AppModule.provideGetEquipmentsUseCase(repository)

        // Injection du UseCase dans le ViewModel via la Factory
        EquipmentViewModelFactory(useCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BeeldiTechTestTheme(
                dynamicColor = false,
            ) {
                val navController = rememberNavController()

                AppNavGraph(
                    navController = navController,
                    equipmentListViewModel = viewModel,
                    equipmentDetailViewModelFactory = { equipment ->
                        EquipmentDetailViewModelFactory(equipment)
                    },
                )
            }
        }
    }
}
