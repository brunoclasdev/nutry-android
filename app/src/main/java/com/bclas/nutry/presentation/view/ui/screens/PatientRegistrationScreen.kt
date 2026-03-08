package com.bclas.nutry.presentation.view.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.bclas.nutry.presentation.viewmodel.AttendanceHistoryItemUiState
import com.bclas.nutry.presentation.viewmodel.PatientRegistrationAction
import com.bclas.nutry.presentation.viewmodel.PatientRegistrationUiState

@Composable
fun PatientRegistrationScreen(
    state: PatientRegistrationUiState,
    onAction: (PatientRegistrationAction) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var newHistoryItem by rememberSaveable { mutableStateOf("") }

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
            text = "Cadastro de pacientes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.fullName,
            onValueChange = { onAction(PatientRegistrationAction.FullNameChanged(it)) },
            label = { Text("Nome completo") },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.sex,
            onValueChange = { onAction(PatientRegistrationAction.SexChanged(it)) },
            label = { Text("Sexo") },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.birthDate,
            onValueChange = { onAction(PatientRegistrationAction.BirthDateChanged(it)) },
            label = { Text("Data de nascimento (dd/mm/aaaa)") },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.phone,
            onValueChange = { onAction(PatientRegistrationAction.PhoneChanged(it)) },
            label = { Text("Telefone") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.email,
            onValueChange = { onAction(PatientRegistrationAction.EmailChanged(it)) },
            label = { Text("E-mail") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.observations,
            onValueChange = { onAction(PatientRegistrationAction.ObservationsChanged(it)) },
            label = { Text("Observacoes") },
            minLines = 3
        )

        Text(
            text = "Foto do paciente",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.patientPhoto,
            onValueChange = { onAction(PatientRegistrationAction.PatientPhotoChanged(it)) },
            label = { Text("URI/caminho da foto") },
            singleLine = true
        )

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        Text(
            text = "Historico de atendimentos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = newHistoryItem,
                onValueChange = { newHistoryItem = it },
                label = { Text("Novo atendimento") },
                minLines = 2
            )
            Button(onClick = {
                onAction(PatientRegistrationAction.AddAttendanceHistory(newHistoryItem))
                newHistoryItem = ""
            }) {
                Text("Adicionar")
            }
        }

        state.attendanceHistory.forEach { item ->
            AttendanceHistoryItem(
                item = item,
                onRemove = {
                    onAction(PatientRegistrationAction.RemoveAttendanceHistory(item.id))
                }
            )
        }
    }
}

@Composable
private fun AttendanceHistoryItem(
    item: AttendanceHistoryItemUiState,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = item.description,
            onValueChange = {},
            readOnly = true,
            label = { Text("Atendimento") }
        )
        TextButton(onClick = onRemove) {
            Text("Remover")
        }
    }
}
