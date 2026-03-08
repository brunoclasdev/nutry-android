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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
    var showSaveConfirmation by rememberSaveable { mutableStateOf(false) }
    var showCancelConfirmation by rememberSaveable { mutableStateOf(false) }

    if (showSaveConfirmation) {
        AlertDialog(
            onDismissRequest = { showSaveConfirmation = false },
            title = { Text("Confirmar salvamento") },
            text = { Text("Deseja salvar esta anamnese nutricional?") },
            confirmButton = {
                TextButton(onClick = {
                    onAction(NutritionalAnamnesisAction.SaveAnamnesis)
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
                    onAction(NutritionalAnamnesisAction.CancelAnamnesis)
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
            onDismissRequest = { onAction(NutritionalAnamnesisAction.DismissFeedback) },
            title = { Text(if (state.feedbackSuccess) "Operacao concluida" else "Falha") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = { onAction(NutritionalAnamnesisAction.DismissFeedback) }) {
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
                text = "Anamnese nutricional",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AnamnesisField(
                        label = "Objetivo do paciente",
                        value = state.patientGoal,
                        onValueChange = { onAction(NutritionalAnamnesisAction.PatientGoalChanged(it)) }
                    )
                },
                second = {
                    AnamnesisField(
                        label = "Rotina alimentar",
                        value = state.dietaryRoutine,
                        onValueChange = { onAction(NutritionalAnamnesisAction.DietaryRoutineChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AnamnesisField(
                        label = "Preferencias alimentares",
                        value = state.dietaryPreferences,
                        onValueChange = { onAction(NutritionalAnamnesisAction.DietaryPreferencesChanged(it)) }
                    )
                },
                second = {
                    AnamnesisField(
                        label = "Restricoes",
                        value = state.restrictions,
                        onValueChange = { onAction(NutritionalAnamnesisAction.RestrictionsChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AnamnesisField(
                        label = "Intolerancias e alergias",
                        value = state.intolerancesAndAllergies,
                        onValueChange = {
                            onAction(NutritionalAnamnesisAction.IntolerancesAndAllergiesChanged(it))
                        }
                    )
                },
                second = {
                    AnamnesisField(
                        label = "Patologias",
                        value = state.pathologies,
                        onValueChange = { onAction(NutritionalAnamnesisAction.PathologiesChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AnamnesisField(
                        label = "Uso de medicamentos",
                        value = state.medicationUse,
                        onValueChange = { onAction(NutritionalAnamnesisAction.MedicationUseChanged(it)) }
                    )
                },
                second = {
                    AnamnesisField(
                        label = "Suplementacao",
                        value = state.supplementation,
                        onValueChange = { onAction(NutritionalAnamnesisAction.SupplementationChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AnamnesisField(
                        label = "Ingestao hidrica",
                        value = state.waterIntake,
                        onValueChange = { onAction(NutritionalAnamnesisAction.WaterIntakeChanged(it)) }
                    )
                },
                second = {
                    AnamnesisField(
                        label = "Sono",
                        value = state.sleep,
                        onValueChange = { onAction(NutritionalAnamnesisAction.SleepChanged(it)) }
                    )
                }
            )

            AdaptiveFieldRow(
                isTablet = isTablet,
                first = {
                    AnamnesisField(
                        label = "Frequencia intestinal",
                        value = state.intestinalFrequency,
                        onValueChange = { onAction(NutritionalAnamnesisAction.IntestinalFrequencyChanged(it)) }
                    )
                },
                second = {
                    AnamnesisField(
                        label = "Nivel de atividade fisica",
                        value = state.physicalActivityLevel,
                        onValueChange = {
                            onAction(NutritionalAnamnesisAction.PhysicalActivityLevelChanged(it))
                        }
                    )
                }
            )

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
