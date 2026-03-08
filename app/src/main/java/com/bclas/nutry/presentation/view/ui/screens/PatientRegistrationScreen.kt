package com.bclas.nutry.presentation.view.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.material3.rememberDatePickerState
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientRegistrationScreen(
    state: PatientRegistrationUiState,
    onAction: (PatientRegistrationAction) -> Unit,
    onBackClick: () -> Unit,
    onViewPatientsClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var newHistoryItem by rememberSaveable { mutableStateOf("") }
    var sexMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var showBirthDatePicker by rememberSaveable { mutableStateOf(false) }
    val calendar = rememberSaveable { Calendar.getInstance() }
    val datePickerState = rememberDatePickerState()
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            onAction(PatientRegistrationAction.PatientPhotoChanged(uri.toString()))
        }
    }
    if (showBirthDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showBirthDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            calendar.timeInMillis = selectedDateMillis
                            val selectedDate = String.format(
                                "%02d/%02d/%04d",
                                calendar.get(Calendar.DAY_OF_MONTH),
                                calendar.get(Calendar.MONTH) + 1,
                                calendar.get(Calendar.YEAR)
                            )
                            onAction(PatientRegistrationAction.BirthDateChanged(selectedDate))
                        }
                        showBirthDatePicker = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBirthDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    state.saveFeedbackMessage?.let { message ->
        AlertDialog(
            onDismissRequest = {
                onAction(PatientRegistrationAction.DismissSaveFeedback)
            },
            title = {
                Text(
                    if (state.saveFeedbackSuccess) "Cadastro salvo" else "Falha no cadastro"
                )
            },
            text = { Text(message) },
            confirmButton = {
                TextButton(
                    onClick = { onAction(PatientRegistrationAction.DismissSaveFeedback) }
                ) {
                    Text("OK")
                }
            }
        )
    }

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
        Text(
            text = "Pacientes cadastrados: ${state.registeredPatients.size}",
            style = MaterialTheme.typography.bodyMedium
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.fullName,
            onValueChange = { onAction(PatientRegistrationAction.FullNameChanged(it)) },
            label = { Text("Nome completo") },
            singleLine = true
        )

        ExposedDropdownMenuBox(
            expanded = sexMenuExpanded,
            onExpandedChange = { sexMenuExpanded = !sexMenuExpanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = state.sex,
                onValueChange = {},
                readOnly = true,
                label = { Text("Sexo") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexMenuExpanded)
                },
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = sexMenuExpanded,
                onDismissRequest = { sexMenuExpanded = false }
            ) {
                listOf("Masculino", "Feminino").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onAction(PatientRegistrationAction.SexChanged(option))
                            sexMenuExpanded = false
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showBirthDatePicker = true
                }
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.birthDate,
                onValueChange = {},
                enabled = false,
                label = { Text("Data de nascimento") },
                placeholder = { Text("Toque para selecionar") },
                singleLine = true
            )
        }

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
            onValueChange = {},
            readOnly = true,
            label = { Text("Foto selecionada") },
            singleLine = true
        )
        Button(
            onClick = { photoPickerLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Escolher foto da galeria")
        }

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

        HorizontalDivider(modifier = Modifier.fillMaxWidth())

        Button(
            onClick = { onAction(PatientRegistrationAction.SavePatient) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar paciente")
        }
        Button(
            onClick = onViewPatientsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver pacientes cadastrados")
        }
        TextButton(
            onClick = { onAction(PatientRegistrationAction.ClearForm) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpar formulario")
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
