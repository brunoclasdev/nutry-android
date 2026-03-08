package com.bclas.nutry.presentation.view.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bclas.nutry.presentation.viewmodel.NutritionalAnamnesisAction
import com.bclas.nutry.presentation.viewmodel.NutritionalAnamnesisUiState

@Composable
fun NutritionalAnamnesisScreen(
    state: NutritionalAnamnesisUiState,
    onAction: (NutritionalAnamnesisAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("Voltar")
        }

        Text(
            text = "Anamnese nutricional",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        AnamnesisField(
            label = "Objetivo do paciente",
            value = state.patientGoal,
            onValueChange = { onAction(NutritionalAnamnesisAction.PatientGoalChanged(it)) }
        )
        AnamnesisField(
            label = "Rotina alimentar",
            value = state.dietaryRoutine,
            onValueChange = { onAction(NutritionalAnamnesisAction.DietaryRoutineChanged(it)) }
        )
        AnamnesisField(
            label = "Preferencias alimentares",
            value = state.dietaryPreferences,
            onValueChange = { onAction(NutritionalAnamnesisAction.DietaryPreferencesChanged(it)) }
        )
        AnamnesisField(
            label = "Restricoes",
            value = state.restrictions,
            onValueChange = { onAction(NutritionalAnamnesisAction.RestrictionsChanged(it)) }
        )
        AnamnesisField(
            label = "Intolerancias e alergias",
            value = state.intolerancesAndAllergies,
            onValueChange = { onAction(NutritionalAnamnesisAction.IntolerancesAndAllergiesChanged(it)) }
        )
        AnamnesisField(
            label = "Patologias",
            value = state.pathologies,
            onValueChange = { onAction(NutritionalAnamnesisAction.PathologiesChanged(it)) }
        )
        AnamnesisField(
            label = "Uso de medicamentos",
            value = state.medicationUse,
            onValueChange = { onAction(NutritionalAnamnesisAction.MedicationUseChanged(it)) }
        )
        AnamnesisField(
            label = "Suplementacao",
            value = state.supplementation,
            onValueChange = { onAction(NutritionalAnamnesisAction.SupplementationChanged(it)) }
        )
        AnamnesisField(
            label = "Ingestao hidrica",
            value = state.waterIntake,
            onValueChange = { onAction(NutritionalAnamnesisAction.WaterIntakeChanged(it)) }
        )
        AnamnesisField(
            label = "Sono",
            value = state.sleep,
            onValueChange = { onAction(NutritionalAnamnesisAction.SleepChanged(it)) }
        )
        AnamnesisField(
            label = "Frequencia intestinal",
            value = state.intestinalFrequency,
            onValueChange = { onAction(NutritionalAnamnesisAction.IntestinalFrequencyChanged(it)) }
        )
        AnamnesisField(
            label = "Nivel de atividade fisica",
            value = state.physicalActivityLevel,
            onValueChange = { onAction(NutritionalAnamnesisAction.PhysicalActivityLevelChanged(it)) }
        )
    }
}

@Composable
private fun AnamnesisField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        minLines = 2
    )
}
