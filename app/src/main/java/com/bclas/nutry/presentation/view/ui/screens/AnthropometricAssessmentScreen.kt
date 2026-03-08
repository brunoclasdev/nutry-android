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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentAction
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentUiState
import com.bclas.nutry.presentation.viewmodel.ProtocolFieldUiState

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
                        label = "Dobras cutaneas (mm)",
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
                    AssessmentNumberField(
                        label = "Massa magra (kg)",
                        value = state.leanMass,
                        onValueChange = { onAction(AnthropometricAssessmentAction.LeanMassChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentNumberField(
                        label = "Massa gorda (kg)",
                        value = state.fatMass,
                        onValueChange = { onAction(AnthropometricAssessmentAction.FatMassChanged(it)) }
                    )
                },
                second = {
                    AssessmentNumberField(
                        label = "Taxa metabolica basal (kcal)",
                        value = state.basalMetabolicRate,
                        onValueChange = { onAction(AnthropometricAssessmentAction.BasalMetabolicRateChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AssessmentNumberField(
                        label = "Gasto energetico total (kcal)",
                        value = state.totalEnergyExpenditure,
                        onValueChange = { onAction(AnthropometricAssessmentAction.TotalEnergyExpenditureChanged(it)) }
                    )
                },
                second = {
                    AssessmentNumberField(
                        label = "Agua corporal (%)",
                        value = state.bodyWater,
                        onValueChange = { onAction(AnthropometricAssessmentAction.BodyWaterChanged(it)) }
                    )
                }
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
