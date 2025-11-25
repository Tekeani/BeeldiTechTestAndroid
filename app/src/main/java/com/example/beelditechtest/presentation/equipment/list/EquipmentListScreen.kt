package com.example.beelditechtest.presentation.equipment.list
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.beelditechtest.domain.model.Equipment
import com.example.beelditechtest.domain.model.UserRole

@Composable
fun EquipmentListLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EquipmentListError(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Erreur: $message",
            color = MaterialTheme.colorScheme.error,
        )
    }
}

@Composable
fun EquipmentListContent(
    equipments: List<Equipment>,
    selectedRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onEquipmentClick: (Equipment) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        RoleSelector(
            selectedRole = selectedRole,
            onRoleSelected = onRoleSelected,
            modifier = Modifier.fillMaxWidth(),
        )

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
        ) {
            items(equipments) { equipment ->
                EquipmentCard(
                    equipment = equipment,
                    onClick = { onEquipmentClick(equipment) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                )
            }
        }
    }
}

@Composable
fun EquipmentListScreen(
    uiState: EquipmentListUiState,
    onRetry: () -> Unit = {},
    onRoleSelected: (UserRole) -> Unit = {},
    onEquipmentClick: (Equipment) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { paddingValues ->
        when (val currentState = uiState) {
            is EquipmentListUiState.Loading -> {
                EquipmentListLoading(
                    modifier = Modifier.padding(paddingValues),
                )
            }
            is EquipmentListUiState.Error -> {
                EquipmentListError(
                    message = currentState.message,
                    modifier = Modifier.padding(paddingValues),
                )
            }
            is EquipmentListUiState.Success -> {
                EquipmentListContent(
                    equipments = currentState.equipments,
                    selectedRole = currentState.selectedRole,
                    onRoleSelected = onRoleSelected,
                    onEquipmentClick = onEquipmentClick,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
}
