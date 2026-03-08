package com.bclas.nutry.presentation.view.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bclas.nutry.presentation.viewmodel.RegisteredPatientUiState

@Composable
fun PatientListScreen(
    patients: List<RegisteredPatientUiState>,
    onBackClick: () -> Unit,
    onNewAssessmentClick: (RegisteredPatientUiState) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = modifier
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(onClick = onBackClick) {
            Text("Voltar")
        }

        Text(
            text = "Pacientes cadastrados",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        if (patients.isEmpty()) {
            Text(
                text = "Nenhum paciente cadastrado ainda.",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(patients, key = { it.id }) { patient ->
                    PatientCard(
                        patient = patient,
                        onNewAssessmentClick = { onNewAssessmentClick(patient) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PatientCard(
    patient: RegisteredPatientUiState,
    onNewAssessmentClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = patient.fullName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (patient.sex.isNotBlank()) {
                Text("Sexo: ${patient.sex}")
            }
            if (patient.birthDate.isNotBlank()) {
                Text("Nascimento: ${patient.birthDate}")
            }
            if (patient.phone.isNotBlank()) {
                Text("Telefone: ${patient.phone}")
            }
            if (patient.email.isNotBlank()) {
                Text("E-mail: ${patient.email}")
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onNewAssessmentClick) {
                    Text("Nova avaliacao")
                }
            }
        }
    }
}
