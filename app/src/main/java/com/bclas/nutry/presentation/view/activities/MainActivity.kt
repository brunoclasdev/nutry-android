package com.bclas.nutry.presentation.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bclas.nutry.presentation.view.ui.screens.AnthropometricAssessmentScreen
import com.bclas.nutry.presentation.view.ui.screens.HomeScreen
import com.bclas.nutry.presentation.view.ui.screens.NutritionalAnamnesisScreen
import com.bclas.nutry.presentation.view.ui.screens.PatientRegistrationScreen
import com.bclas.nutry.presentation.view.ui.theme.NutryTheme
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentUiState
import com.bclas.nutry.presentation.viewmodel.AnthropometricAssessmentViewModel
import com.bclas.nutry.presentation.viewmodel.MainAction
import com.bclas.nutry.presentation.viewmodel.MainViewModel
import com.bclas.nutry.presentation.viewmodel.NutryScreen
import com.bclas.nutry.presentation.viewmodel.NutritionalAnamnesisUiState
import com.bclas.nutry.presentation.viewmodel.NutritionalAnamnesisViewModel
import com.bclas.nutry.presentation.viewmodel.PatientRegistrationUiState
import com.bclas.nutry.presentation.viewmodel.PatientRegistrationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutryTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val mainViewModel: MainViewModel = viewModel()
                    val anthropometricAssessmentViewModel: AnthropometricAssessmentViewModel = viewModel()
                    val patientRegistrationViewModel: PatientRegistrationViewModel = viewModel()
                    val nutritionalAnamnesisViewModel: NutritionalAnamnesisViewModel = viewModel()

                    when (mainViewModel.uiState.currentScreen) {
                        NutryScreen.HOME -> {
                            HomeScreen(
                                onRegisterPatientClick = {
                                    mainViewModel.onAction(MainAction.NavigateTo(NutryScreen.PATIENT_REGISTRATION))
                                },
                                onAnthropometricAssessmentClick = {
                                    mainViewModel.onAction(MainAction.NavigateTo(NutryScreen.ANTHROPOMETRIC_ASSESSMENT))
                                },
                                onNutritionalAnamnesisClick = {
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
                                onBackClick = { mainViewModel.onAction(MainAction.NavigateBackHome) },
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            )
                        }

                        NutryScreen.ANTHROPOMETRIC_ASSESSMENT -> {
                            AnthropometricAssessmentScreen(
                                state = anthropometricAssessmentViewModel.uiState,
                                onAction = anthropometricAssessmentViewModel::onAction,
                                onBackClick = { mainViewModel.onAction(MainAction.NavigateBackHome) },
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = innerPadding
                            )
                        }

                        NutryScreen.NUTRITIONAL_ANAMNESIS -> {
                            NutritionalAnamnesisScreen(
                                state = nutritionalAnamnesisViewModel.uiState,
                                onAction = nutritionalAnamnesisViewModel::onAction,
                                onBackClick = { mainViewModel.onAction(MainAction.NavigateBackHome) },
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
            onAnthropometricAssessmentClick = {},
            onNutritionalAnamnesisClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PatientRegistrationScreenPreview() {
    NutryTheme {
        PatientRegistrationScreen(
            state = PatientRegistrationUiState(),
            onAction = {},
            onBackClick = {}
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
            onBackClick = {}
        )
    }
}
