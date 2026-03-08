package com.bclas.nutry.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class NutryScreen {
    HOME,
    PATIENT_REGISTRATION,
    PATIENT_LIST,
    PATIENT_HISTORY,
    NUTRITIONAL_ANAMNESIS,
    ANTHROPOMETRIC_ASSESSMENT,
    ASSESSMENT_FINAL
}

data class MainUiState(
    val currentScreen: NutryScreen = NutryScreen.HOME,
    val selectedPatientId: Long? = null
)

sealed interface MainAction {
    data class NavigateTo(val screen: NutryScreen) : MainAction
    data class StartPatientAssessment(val patientId: Long) : MainAction
    data class ViewPatientHistory(val patientId: Long) : MainAction
    data object ContinueToAnthropometricAssessment : MainAction
    data object FinishAssessment : MainAction
    data object NavigateBackHome : MainAction
}

class MainViewModel : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.NavigateTo -> {
                uiState = uiState.copy(
                    currentScreen = action.screen,
                    selectedPatientId = if (
                        action.screen == NutryScreen.NUTRITIONAL_ANAMNESIS ||
                        action.screen == NutryScreen.ANTHROPOMETRIC_ASSESSMENT ||
                        action.screen == NutryScreen.ASSESSMENT_FINAL ||
                        action.screen == NutryScreen.PATIENT_HISTORY
                    ) {
                        uiState.selectedPatientId
                    } else {
                        null
                    }
                )
            }

            is MainAction.StartPatientAssessment -> {
                uiState = uiState.copy(
                    currentScreen = NutryScreen.NUTRITIONAL_ANAMNESIS,
                    selectedPatientId = action.patientId
                )
            }

            is MainAction.ViewPatientHistory -> {
                uiState = uiState.copy(
                    currentScreen = NutryScreen.PATIENT_HISTORY,
                    selectedPatientId = action.patientId
                )
            }

            MainAction.ContinueToAnthropometricAssessment -> {
                uiState = uiState.copy(
                    currentScreen = NutryScreen.ANTHROPOMETRIC_ASSESSMENT
                )
            }

            MainAction.FinishAssessment -> {
                uiState = uiState.copy(
                    currentScreen = NutryScreen.ASSESSMENT_FINAL
                )
            }

            MainAction.NavigateBackHome -> {
                uiState = uiState.copy(
                    currentScreen = NutryScreen.HOME,
                    selectedPatientId = null
                )
            }
        }
    }
}
