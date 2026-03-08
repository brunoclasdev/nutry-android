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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentUiState
import com.bclas.nutry.presentation.viewmodel.NutritionalAnamnesisUiState
import com.bclas.nutry.presentation.viewmodel.RegisteredPatientUiState

@Composable
fun AssessmentFinalScreen(
    patient: RegisteredPatientUiState?,
    anamnesis: NutritionalAnamnesisUiState,
    anthropometric: AnthropometricAssessmentUiState,
    onBackHomeClick: () -> Unit,
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
            TextButton(onClick = onBackHomeClick) {
                Text("Voltar ao inicio")
            }

            Text(
                text = "Resumo final da avaliacao",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Paciente", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("Nome: ${patient?.fullName ?: "Nao informado"}")
                    Text("Sexo: ${patient?.sex ?: ""}")
                    Text("Nascimento: ${patient?.birthDate ?: ""}")
                    Text("Telefone: ${patient?.phone ?: ""}")
                    Text("E-mail: ${patient?.email ?: ""}")
                    Text("Observacoes: ${patient?.observations ?: ""}")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Anamnese nutricional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("Objetivo: ${anamnesis.patientGoal}")
                    Text("Rotina alimentar: ${anamnesis.dietaryRoutine}")
                    Text("Preferencias: ${anamnesis.dietaryPreferences}")
                    Text("Restricoes: ${anamnesis.restrictions}")
                    Text("Intolerancias/alergias: ${anamnesis.intolerancesAndAllergies}")
                    Text("Patologias: ${anamnesis.pathologies}")
                    Text("Medicamentos: ${anamnesis.medicationUse}")
                    Text("Suplementacao: ${anamnesis.supplementation}")
                    Text("Ingestao hidrica: ${anamnesis.waterIntake}")
                    Text("Sono: ${anamnesis.sleep}")
                    Text("Frequencia intestinal: ${anamnesis.intestinalFrequency}")
                    Text("Nivel atividade fisica: ${anamnesis.physicalActivityLevel}")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Avaliacao antropometrica", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("Peso: ${anthropometric.weight} kg")
                    Text("Altura: ${anthropometric.height}")
                    Text("Idade: ${anthropometric.age}")
                    Text("Sexo biologico: ${anthropometric.biologicalSex}")
                    Text("Nivel atividade: ${anthropometric.activityLevel}")
                    Text("IMC: ${anthropometric.bmi}")
                    Text("Cintura/quadril: ${anthropometric.waistHipRatio}")
                    Text("Dobras: ${anthropometric.skinfolds}")
                    Text("% Gordura: ${anthropometric.bodyFatPercentage}")
                    Text("Massa gorda: ${anthropometric.fatMass} kg")
                    Text("Massa magra: ${anthropometric.leanMass} kg")
                    Text("TMB: ${anthropometric.basalMetabolicRate} kcal")
                    Text("GET: ${anthropometric.totalEnergyExpenditure} kcal")
                    Text("Agua corporal: ${anthropometric.bodyWater}%")
                }
            }

            Button(
                onClick = onBackHomeClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar fluxo")
            }
        }
    }
}
