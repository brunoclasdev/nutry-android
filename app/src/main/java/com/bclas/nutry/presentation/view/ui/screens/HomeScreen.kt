package com.bclas.nutry.presentation.view.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onRegisterPatientClick: () -> Unit,
    onPatientListClick: () -> Unit,
    onAnthropometricAssessmentClick: () -> Unit,
    onNutritionalAnamnesisClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Nutry",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Selecione a funcionalidade desejada.",
            style = MaterialTheme.typography.bodyMedium
        )

        Button(
            onClick = onRegisterPatientClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cadastro de pacientes")
        }
        Button(
            onClick = onPatientListClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pacientes cadastrados")
        }

        Button(
            onClick = onAnthropometricAssessmentClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Avaliacao antropometrica")
        }

        Button(
            onClick = onNutritionalAnamnesisClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Anamnese nutricional")
        }
    }
}
