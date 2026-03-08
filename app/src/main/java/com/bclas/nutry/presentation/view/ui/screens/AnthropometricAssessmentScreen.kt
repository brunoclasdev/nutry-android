package com.bclas.nutry.presentation.view.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentAction
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentUiState
import com.bclas.nutry.presentation.viewmodel.ProtocolFieldUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnthropometricAssessmentScreen(
    state: AnthropometricAssessmentUiState,
    onAction: (AnthropometricAssessmentAction) -> Unit,
    onBackClick: () -> Unit = {},
    patientName: String? = null,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var protocolFieldName by rememberSaveable { mutableStateOf("") }
    var showSaveConfirmation by rememberSaveable { mutableStateOf(false) }
    var showCancelConfirmation by rememberSaveable { mutableStateOf(false) }
    var sexMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var activityMenuExpanded by rememberSaveable { mutableStateOf(false) }

    if (showSaveConfirmation) {
        AlertDialog(
            onDismissRequest = { showSaveConfirmation = false },
            title = { Text("Confirmar salvamento") },
            text = { Text("Deseja salvar esta avaliacao antropometrica?") },
            confirmButton = {
                TextButton(onClick = {
                    onAction(AnthropometricAssessmentAction.SaveAssessment)
                    showSaveConfirmation = false
                }) { Text("Salvar") }
            },
            dismissButton = {
                TextButton(onClick = { showSaveConfirmation = false }) { Text("Voltar") }
            }
        )
    }
    if (showCancelConfirmation) {
        AlertDialog(
            onDismissRequest = { showCancelConfirmation = false },
            title = { Text("Confirmar cancelamento") },
            text = { Text("Deseja cancelar? Os dados preenchidos serao limpos.") },
            confirmButton = {
                TextButton(onClick = {
                    onAction(AnthropometricAssessmentAction.CancelAssessment)
                    showCancelConfirmation = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showCancelConfirmation = false }) { Text("Voltar") }
            }
        )
    }
    state.feedbackMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { onAction(AnthropometricAssessmentAction.DismissFeedback) },
            title = {
                Text(if (state.feedbackSuccess) "Operacao concluida" else "Falha")
            },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { onAction(AnthropometricAssessmentAction.DismissFeedback) }) {
                    Text("OK")
                }
            }
        )
    }

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(onClick = onBackClick) {
                Text("Voltar")
            }

            Text(
                text = "Avaliacao antropometrica",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            if (!patientName.isNullOrBlank()) {
                Text(
                    text = "Paciente: $patientName",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Text(
                text = "Preencha os dados do paciente. IMC e relacao cintura/quadril sao calculados automaticamente.",
                style = MaterialTheme.typography.bodyMedium
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentNumberField(
                        label = "Peso (kg)",
                        value = state.weight,
                        onValueChange = { onAction(AnthropometricAssessmentAction.WeightChanged(it)) }
                    )
                },
                second = {
                    AssessmentNumberField(
                        label = "Altura (m ou cm)",
                        value = state.height,
                        onValueChange = { onAction(AnthropometricAssessmentAction.HeightChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentNumberField(
                        label = "Idade (anos)",
                        value = state.age,
                        onValueChange = { onAction(AnthropometricAssessmentAction.AgeChanged(it)) }
                    )
                },
                second = {
                    ExposedDropdownMenuBox(
                        expanded = sexMenuExpanded,
                        onExpandedChange = { sexMenuExpanded = !sexMenuExpanded }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            value = state.biologicalSex,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Sexo biológico") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexMenuExpanded)
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = sexMenuExpanded,
                            onDismissRequest = { sexMenuExpanded = false }
                        ) {
                            listOf("Masculino", "Feminino").forEach { sex ->
                                DropdownMenuItem(
                                    text = { Text(sex) },
                                    onClick = {
                                        onAction(AnthropometricAssessmentAction.BiologicalSexChanged(sex))
                                        sexMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentReadOnlyField(
                        label = "IMC",
                        value = state.bmi
                    )
                },
                second = {
                    AssessmentNumberField(
                        label = "Circunferencia abdominal (cm)",
                        value = state.abdominalCircumference,
                        onValueChange = {
                            onAction(AnthropometricAssessmentAction.AbdominalCircumferenceChanged(it))
                        }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentNumberField(
                        label = "Circunferencia de cintura (cm)",
                        value = state.waistCircumference,
                        onValueChange = { onAction(AnthropometricAssessmentAction.WaistCircumferenceChanged(it)) }
                    )
                },
                second = {
                    AssessmentNumberField(
                        label = "Circunferencia de quadril (cm)",
                        value = state.hipCircumference,
                        onValueChange = { onAction(AnthropometricAssessmentAction.HipCircumferenceChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentReadOnlyField(
                        label = "Relacao cintura/quadril",
                        value = state.waistHipRatio
                    )
                },
                second = {
                    AssessmentNumberField(
                        label = "Somatorio de dobras cutaneas (mm)",
                        value = state.skinfolds,
                        onValueChange = { onAction(AnthropometricAssessmentAction.SkinfoldsChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentNumberField(
                        label = "Percentual de gordura corporal (%)",
                        value = state.bodyFatPercentage,
                        onValueChange = { onAction(AnthropometricAssessmentAction.BodyFatPercentageChanged(it)) }
                    )
                },
                second = {
                    AssessmentReadOnlyField(
                        label = "Massa magra (kg)",
                        value = state.leanMass,
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentReadOnlyField(
                        label = "Massa gorda (kg)",
                        value = state.fatMass,
                    )
                },
                second = {
                    AssessmentReadOnlyField(
                        label = "Taxa metabolica basal (kcal)",
                        value = state.basalMetabolicRate,
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    ExposedDropdownMenuBox(
                        expanded = activityMenuExpanded,
                        onExpandedChange = { activityMenuExpanded = !activityMenuExpanded }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            value = state.activityLevel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Nivel de atividade") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = activityMenuExpanded)
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = activityMenuExpanded,
                            onDismissRequest = { activityMenuExpanded = false }
                        ) {
                            listOf("Sedentario", "Leve", "Moderado", "Intenso", "Muito intenso").forEach { level ->
                                DropdownMenuItem(
                                    text = { Text(level) },
                                    onClick = {
                                        onAction(AnthropometricAssessmentAction.ActivityLevelChanged(level))
                                        activityMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                second = {
                    AssessmentReadOnlyField(
                        label = "Gasto energetico total (kcal)",
                        value = state.totalEnergyExpenditure
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentReadOnlyField(
                        label = "Agua corporal (%)",
                        value = state.bodyWater
                    )
                },
                second = { }
            )

            Text(
                text = "Obs.: % de gordura e agua corporal sao estimados automaticamente quando ha dados suficientes.",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Para % gordura por dobras: informar somatorio de dobras, idade e sexo.",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "TMB usa Katch-McArdle (massa magra), com fallback para Mifflin-St Jeor.",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "GET = TMB x fator de atividade.",
                style = MaterialTheme.typography.bodySmall
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Text(
                text = "Campos configuraveis (protocolos)",
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
                    value = protocolFieldName,
                    onValueChange = { protocolFieldName = it },
                    label = { Text("Nome do campo") },
                    singleLine = true
                )
                Button(
                    onClick = {
                        onAction(AnthropometricAssessmentAction.AddProtocolField(protocolFieldName))
                        protocolFieldName = ""
                    }
                ) {
                    Text("Adicionar")
                }
            }

            state.protocolFields.forEach { field ->
                ConfigurableProtocolField(
                    field = field,
                    onValueChange = {
                        onAction(
                            AnthropometricAssessmentAction.ProtocolFieldValueChanged(
                                fieldId = field.id,
                                value = it
                            )
                        )
                    },
                    onRemove = {
                        onAction(AnthropometricAssessmentAction.RemoveProtocolField(field.id))
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    Button(
                        onClick = { showCancelConfirmation = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar")
                    }
                },
                second = {
                    Button(
                        onClick = { showSaveConfirmation = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Salvar")
                    }
                }
            )
        }
    }
}

@Composable
private fun AdaptiveFieldRow(
    isTablet: Boolean,
    first: @Composable () -> Unit,
    second: @Composable () -> Unit
) {
    if (isTablet) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = Modifier.weight(1f)) { first() }
            Box(modifier = Modifier.weight(1f)) { second() }
        }
    } else {
        first()
        second()
    }
}

@Composable
private fun AssessmentNumberField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
    )
}

@Composable
private fun AssessmentReadOnlyField(
    label: String,
    value: String
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {},
        label = { Text(label) },
        singleLine = true,
        readOnly = true
    )
}

@Composable
private fun ConfigurableProtocolField(
    field: ProtocolFieldUiState,
    onValueChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = field.value,
            onValueChange = onValueChange,
            label = { Text(field.name) },
            singleLine = true
        )
        TextButton(onClick = onRemove) {
            Text("Remover")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AnthropometricAssessmentScreenPreview() {
    AnthropometricAssessmentScreen(
        state = AnthropometricAssessmentUiState(
            weight = "70",
            height = "1.72",
            bmi = "23.66",
            waistCircumference = "80",
            hipCircumference = "100",
            waistHipRatio = "0.80",
            protocolFields = listOf(
                ProtocolFieldUiState(1, "Perimetro de coxa", "58"),
                ProtocolFieldUiState(2, "Somatorio 7 dobras", "92")
            )
        ),
        onAction = {}
    )
}
