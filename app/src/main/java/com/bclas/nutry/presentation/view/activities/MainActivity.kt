package com.bclas.nutry.presentation.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bclas.nutry.core.di.AppContainer
import com.bclas.nutry.domain.model.AnthropometricData
import com.bclas.nutry.domain.model.NutritionalAnamnesisData
import com.bclas.nutry.presentation.view.ui.screens.AssessmentFinalScreen
import com.bclas.nutry.presentation.view.ui.screens.AnthropometricAssessmentScreen
import com.bclas.nutry.presentation.view.ui.screens.HomeScreen
import com.bclas.nutry.presentation.view.ui.screens.NutritionalAnamnesisScreen
import com.bclas.nutry.presentation.view.ui.screens.PatientHistoryScreen
import com.bclas.nutry.presentation.view.ui.screens.PatientListScreen
import com.bclas.nutry.presentation.view.ui.screens.PatientRegistrationScreen
import com.bclas.nutry.presentation.view.ui.theme.NutryTheme
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentUiState
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentViewModel
import com.bclas.nutry.presentation.viewmodel.MainAction
import com.bclas.nutry.presentation.viewmodel.MainViewModel
import com.bclas.nutry.presentation.viewmodel.NutryScreen
import com.bclas.nutry.presentation.viewmodel.NutritionalAnamnesisUiState
import com.bclas.nutry.presentation.viewmodel.NutritionalAnamnesisViewModel
import com.bclas.nutry.presentation.viewmodel.NutritionalAnamnesisAction
import com.bclas.nutry.presentation.viewmodel.PatientRegistrationUiState
import com.bclas.nutry.presentation.viewmodel.PatientRegistrationAction
import com.bclas.nutry.presentation.viewmodel.PatientRegistrationViewModel
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentAction

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppContainer.initialize(applicationContext)
        enableEdgeToEdge()
        setContent {
            NutryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val mainViewModel: MainViewModel = viewModel()
                    val anthropometricAssessmentViewModel: AnthropometricAssessmentViewModel = viewModel()
                    val patientRegistrationViewModel: PatientRegistrationViewModel = viewModel()
                    val nutritionalAnamnesisViewModel: NutritionalAnamnesisViewModel = viewModel()
                    val selectedPatient = patientRegistrationViewModel.uiState.registeredPatients
                        .firstOrNull { it.id == mainViewModel.uiState.selectedPatientId }
                    LaunchedEffect(mainViewModel.uiState.currentScreen) {
                        if (
                            mainViewModel.uiState.currentScreen == NutryScreen.PATIENT_LIST ||
                            mainViewModel.uiState.currentScreen == NutryScreen.PATIENT_HISTORY ||
                            mainViewModel.uiState.currentScreen == NutryScreen.ASSESSMENT_FINAL
                        ) {
                            patientRegistrationViewModel.onAction(PatientRegistrationAction.ReloadPatients)
                        }
                    }
                    BackHandler(enabled = mainViewModel.uiState.currentScreen != NutryScreen.HOME) {
                        mainViewModel.onAction(MainAction.NavigateBack)
                    }

                    when (mainViewModel.uiState.currentScreen) {
                        NutryScreen.HOME -> {
                            HomeScreen(
                                onRegisterPatientClick = {
                                    patientRegistrationViewModel.onAction(PatientRegistrationAction.ClearForm)
                                    mainViewModel.onAction(MainAction.NavigateTo(NutryScreen.PATIENT_REGISTRATION))
                                },
                                onPatientListClick = {
                                    mainViewModel.onAction(MainAction.NavigateTo(NutryScreen.PATIENT_LIST))
                                },
                                onAnthropometricAssessmentClick = {
                                    anthropometricAssessmentViewModel.onAction(AnthropometricAssessmentAction.ClearForm)
                                    mainViewModel.onAction(MainAction.NavigateTo(NutryScreen.ANTHROPOMETRIC_ASSESSMENT))
                                },
                                onNutritionalAnamnesisClick = {
                                    nutritionalAnamnesisViewModel.onAction(NutritionalAnamnesisAction.ClearForm)
                                    mainViewModel.onAction(MainAction.NavigateTo(NutryScreen.NUTRITIONAL_ANAMNESIS))
                                },
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            )
                        }

                        NutryScreen.PATIENT_REGISTRATION -> {
                            PatientRegistrationScreen(
                                state = patientRegistrationViewModel.uiState,
                                onAction = patientRegistrationViewModel::onAction,
                                onBackClick = {
                                    patientRegistrationViewModel.onAction(PatientRegistrationAction.ClearForm)
                                    mainViewModel.onAction(MainAction.NavigateBack)
                                },
                                onViewPatientsClick = {
                                    patientRegistrationViewModel.onAction(PatientRegistrationAction.ClearForm)
                                    mainViewModel.onAction(MainAction.NavigateTo(NutryScreen.PATIENT_LIST))
                                },
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            )
                        }

                        NutryScreen.PATIENT_LIST -> {
                            PatientListScreen(
                                patients = patientRegistrationViewModel.uiState.registeredPatients,
                                onBackClick = { mainViewModel.onAction(MainAction.NavigateBack) },
                                onNewAssessmentClick = { patient ->
                                    nutritionalAnamnesisViewModel.onAction(NutritionalAnamnesisAction.ClearForm)
                                    anthropometricAssessmentViewModel.onAction(AnthropometricAssessmentAction.ClearForm)
                                    mainViewModel.onAction(
                                        MainAction.StartPatientAssessment(patient.id)
                                    )
                                },
                                onViewHistoryClick = { patient ->
                                    mainViewModel.onAction(MainAction.ViewPatientHistory(patient.id))
                                },
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            )
                        }

                        NutryScreen.PATIENT_HISTORY -> {
                            PatientHistoryScreen(
                                patient = selectedPatient,
                                onBackClick = { mainViewModel.onAction(MainAction.NavigateBack) },
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            )
                        }

                        NutryScreen.NUTRITIONAL_ANAMNESIS -> {
                            NutritionalAnamnesisScreen(
                                state = nutritionalAnamnesisViewModel.uiState,
                                onAction = nutritionalAnamnesisViewModel::onAction,
                                onBackClick = {
                                    mainViewModel.onAction(MainAction.NavigateBack)
                                },
                                onProceedClick = {
                                    mainViewModel.onAction(MainAction.ContinueToAnthropometricAssessment)
                                },
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            )
                        }

                        NutryScreen.ANTHROPOMETRIC_ASSESSMENT -> {
                            AnthropometricAssessmentScreen(
                                state = anthropometricAssessmentViewModel.uiState,
                                onAction = anthropometricAssessmentViewModel::onAction,
                                onBackClick = {
                                    mainViewModel.onAction(MainAction.NavigateBack)
                                },
                                onFinishClick = {
                                    val selectedPatientId = mainViewModel.uiState.selectedPatientId
                                    if (selectedPatientId != null) {
                                        val saved = patientRegistrationViewModel.saveAssessmentForPatient(
                                            patientId = selectedPatientId,
                                            anamnesis = nutritionalAnamnesisViewModel.uiState.toDomain(),
                                            anthropometric = anthropometricAssessmentViewModel.uiState.toDomain()
                                        )
                                        if (saved) {
                                            mainViewModel.onAction(MainAction.FinishAssessment)
                                        }
                                    }
                                },
                                patientName = selectedPatient?.fullName,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            )
                        }

                        NutryScreen.ASSESSMENT_FINAL -> {
                            AssessmentFinalScreen(
                                patient = selectedPatient,
                                anamnesis = nutritionalAnamnesisViewModel.uiState,
                                anthropometric = anthropometricAssessmentViewModel.uiState,
                                onBackHomeClick = {
                                    nutritionalAnamnesisViewModel.onAction(NutritionalAnamnesisAction.ClearForm)
                                    anthropometricAssessmentViewModel.onAction(AnthropometricAssessmentAction.ClearForm)
                                    mainViewModel.onAction(MainAction.NavigateBackHome)
                                },
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    NutryTheme {
        HomeScreen(
            onRegisterPatientClick = {},
            onPatientListClick = {},
            onAnthropometricAssessmentClick = {},
            onNutritionalAnamnesisClick = {}
        )
    }
}

private fun NutritionalAnamnesisUiState.toDomain(): NutritionalAnamnesisData {
    return NutritionalAnamnesisData(
        patientGoal = patientGoal,
        dietaryRoutine = dietaryRoutine,
        dietaryPreferences = dietaryPreferences,
        restrictions = restrictions,
        intolerancesAndAllergies = intolerancesAndAllergies,
        pathologies = pathologies,
        medicationUse = medicationUse,
        supplementation = supplementation,
        waterIntake = waterIntake,
        sleep = sleep,
        intestinalFrequency = intestinalFrequency,
        physicalActivityLevel = physicalActivityLevel
    )
}

private fun AnthropometricAssessmentUiState.toDomain(): AnthropometricData {
    return AnthropometricData(
        weight = weight,
        height = height,
        age = age,
        biologicalSex = biologicalSex,
        activityLevel = activityLevel,
        bmi = bmi,
        abdominalCircumference = abdominalCircumference,
        waistCircumference = waistCircumference,
        hipCircumference = hipCircumference,
        waistHipRatio = waistHipRatio,
        skinfolds = skinfolds,
        bodyFatPercentage = bodyFatPercentage,
        leanMass = leanMass,
        fatMass = fatMass,
        basalMetabolicRate = basalMetabolicRate,
        totalEnergyExpenditure = totalEnergyExpenditure,
        bodyWater = bodyWater
    )
}

@Preview(showBackground = true)
@Composable
fun PatientRegistrationScreenPreview() {
    NutryTheme {
        PatientRegistrationScreen(
            state = PatientRegistrationUiState(),
            onAction = {},
            onBackClick = {},
            onViewPatientsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnthropometricAssessmentPreview() {
    NutryTheme {
        AnthropometricAssessmentScreen(
            state = AnthropometricAssessmentUiState(),
            onAction = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NutritionalAnamnesisPreview() {
    NutryTheme {
        NutritionalAnamnesisScreen(
            state = NutritionalAnamnesisUiState(),
            onAction = {},
            onBackClick = {},
            onProceedClick = {}
        )
    }
}
