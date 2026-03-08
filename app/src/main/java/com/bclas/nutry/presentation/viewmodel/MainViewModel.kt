package com.bclas.nutry.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class NutryScreen {
    HOME,
    PATIENT_REGISTRATION,
    PATIENT_LIST,
    ANTHROPOMETRIC_ASSESSMENT,
    NUTRITIONAL_ANAMNESIS
}

data class MainUiState(
    val currentScreen: NutryScreen = NutryScreen.HOME,
    val selectedPatientName: String? = null
)

sealed interface MainAction {
    data class NavigateTo(val screen: NutryScreen) : MainAction
    data class StartPatientAssessment(val patientName: String) : MainAction
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
                    selectedPatientName = if (action.screen == NutryScreen.ANTHROPOMETRIC_ASSESSMENT) {
                        uiState.selectedPatientName
                    } else {
                        null
                    }
                )
            }

            is MainAction.StartPatientAssessment -> {
                uiState = uiState.copy(
                    currentScreen = NutryScreen.ANTHROPOMETRIC_ASSESSMENT,
                    selectedPatientName = action.patientName
                )
            }

            MainAction.NavigateBackHome -> {
                uiState = uiState.copy(
                    currentScreen = NutryScreen.HOME,
                    selectedPatientName = null
                )
            }
        }
    }
}
