package com.bclas.nutry.presentation.view.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bclas.nutry.presentation.viewmodel.RegisteredPatientUiState

@Composable
fun PatientHistoryScreen(
    patient: RegisteredPatientUiState?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    BoxWithConstraints(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        val isTablet = maxWidth >= 840.dp

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = if (isTablet) 980.dp else 560.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = onBackClick) {
                Text("Voltar")
            }

            Text(
                text = "Historico de avaliacoes",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Paciente: ${patient?.fullName ?: "Nao informado"}",
                style = MaterialTheme.typography.titleMedium
            )

            val records = patient?.assessmentHistory.orEmpty().reversed()
            if (records.isEmpty()) {
                Text("Nenhuma avaliacao registrada para este paciente.")
            } else {
                records.forEach { record ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Avaliacao em ${record.createdAt}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text("Objetivo: ${record.anamnesis.patientGoal}")
                            Text("Rotina alimentar: ${record.anamnesis.dietaryRoutine}")
                            Text("Peso: ${record.anthropometric.weight} kg")
                            Text("Altura: ${record.anthropometric.height}")
                            Text("IMC: ${record.anthropometric.bmi}")
                            Text("% Gordura: ${record.anthropometric.bodyFatPercentage}")
                            Text("Massa gorda: ${record.anthropometric.fatMass} kg")
                            Text("Massa magra: ${record.anthropometric.leanMass} kg")
                            Text("TMB: ${record.anthropometric.basalMetabolicRate} kcal")
                            Text("GET: ${record.anthropometric.totalEnergyExpenditure} kcal")
                        }
                    }
                }
            }
        }
    }
}
