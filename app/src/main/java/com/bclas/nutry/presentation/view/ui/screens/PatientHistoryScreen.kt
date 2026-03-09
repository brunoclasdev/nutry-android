package com.bclas.nutry.presentation.view.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bclas.nutry.presentation.viewmodel.PatientAssessmentRecordUiState
import com.bclas.nutry.presentation.viewmodel.RegisteredPatientUiState

@Composable
fun PatientHistoryScreen(
    patient: RegisteredPatientUiState?,
    onBackClick: () -> Unit,
    onDeleteAssessmentClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    var selectedAssessmentId by rememberSaveable { mutableStateOf<Long?>(null) }
    var pendingDeleteAssessmentId by rememberSaveable { mutableStateOf<Long?>(null) }
    val records = patient?.assessmentHistory.orEmpty().reversed()
    val selectedRecord = records.firstOrNull { it.id == selectedAssessmentId }

    pendingDeleteAssessmentId?.let { assessmentId ->
        AlertDialog(
            onDismissRequest = { pendingDeleteAssessmentId = null },
            title = { Text("Excluir avaliacao") },
            text = { Text("Deseja realmente excluir esta avaliacao do historico?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        pendingDeleteAssessmentId = null
                        if (selectedAssessmentId == assessmentId) {
                            selectedAssessmentId = null
                        }
                        onDeleteAssessmentClick(assessmentId)
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteAssessmentId = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    BoxWithConstraints(
        modifier = modifier
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

            if (records.isEmpty()) {
                Text("Nenhuma avaliacao registrada para este paciente.")
            } else if (selectedRecord == null) {
                Text(
                    text = "Selecione uma avaliacao para ver os detalhes.",
                    style = MaterialTheme.typography.bodyMedium
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(records, key = { it.id }) { record ->
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
                                Text("IMC: ${record.anthropometric.bmi}")
                                Text("Objetivo: ${record.anamnesis.patientGoal}")
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    TextButton(onClick = { selectedAssessmentId = record.id }) {
                                        Text("Ver detalhes")
                                    }
                                    TextButton(onClick = { pendingDeleteAssessmentId = record.id }) {
                                        Text("Excluir avaliacao")
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                TextButton(onClick = { selectedAssessmentId = null }) {
                    Text("Voltar para lista")
                }
                AssessmentHistoryDetail(
                    record = selectedRecord,
                    onRequestDeleteAssessmentClick = { pendingDeleteAssessmentId = it }
                )
            }
        }
    }
}

@Composable
private fun AssessmentHistoryDetail(
    record: PatientAssessmentRecordUiState,
    onRequestDeleteAssessmentClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
                Text(
                    text = "Anamnese nutricional",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text("Objetivo: ${record.anamnesis.patientGoal}")
                Text("Rotina alimentar: ${record.anamnesis.dietaryRoutine}")
                Text("Preferencias alimentares: ${record.anamnesis.dietaryPreferences}")
                Text("Restricoes: ${record.anamnesis.restrictions}")
                Text("Intolerancias/alergias: ${record.anamnesis.intolerancesAndAllergies}")
                Text("Patologias: ${record.anamnesis.pathologies}")
                Text("Uso de medicamentos: ${record.anamnesis.medicationUse}")
                Text("Suplementacao: ${record.anamnesis.supplementation}")
                Text("Ingestao hidrica: ${record.anamnesis.waterIntake}")
                Text("Sono: ${record.anamnesis.sleep}")
                Text("Frequencia intestinal: ${record.anamnesis.intestinalFrequency}")
                Text("Nivel de atividade fisica: ${record.anamnesis.physicalActivityLevel}")

                Text(
                    text = "Avaliacao antropometrica",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text("Peso: ${record.anthropometric.weight} kg")
                Text("Altura: ${record.anthropometric.height}")
                Text("Idade: ${record.anthropometric.age}")
                Text("Sexo biologico: ${record.anthropometric.biologicalSex}")
                Text("Nivel de atividade: ${record.anthropometric.activityLevel}")
                Text("IMC: ${record.anthropometric.bmi}")
                Text("Circ. abdominal: ${record.anthropometric.abdominalCircumference}")
                Text("Circ. cintura: ${record.anthropometric.waistCircumference}")
                Text("Circ. quadril: ${record.anthropometric.hipCircumference}")
                Text("Relacao cintura/quadril: ${record.anthropometric.waistHipRatio}")
                Text("Dobras cutaneas: ${record.anthropometric.skinfolds}")
                Text("% Gordura: ${record.anthropometric.bodyFatPercentage}")
                Text("Massa gorda: ${record.anthropometric.fatMass} kg")
                Text("Massa magra: ${record.anthropometric.leanMass} kg")
                Text("TMB: ${record.anthropometric.basalMetabolicRate} kcal")
                Text("GET: ${record.anthropometric.totalEnergyExpenditure} kcal")
                Text("Agua corporal: ${record.anthropometric.bodyWater}%")
                TextButton(onClick = { onRequestDeleteAssessmentClick(record.id) }) {
                    Text("Excluir avaliacao")
                }
            }
        }
    }
}
