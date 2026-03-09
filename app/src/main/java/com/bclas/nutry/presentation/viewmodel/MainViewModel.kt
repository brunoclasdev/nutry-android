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

data class NavigationEntry(
    val screen: NutryScreen,
    val selectedPatientId: Long? = null
)

data class MainUiState(
    val currentScreen: NutryScreen = NutryScreen.HOME,
    val selectedPatientId: Long? = null,
    val backStack: List<NavigationEntry> = listOf(
        NavigationEntry(screen = NutryScreen.HOME, selectedPatientId = null)
    )
)

sealed interface MainAction {
    data class NavigateTo(val screen: NutryScreen) : MainAction
    data class StartPatientAssessment(val patientId: Long) : MainAction
    data class ViewPatientHistory(val patientId: Long) : MainAction
    data object ContinueToAnthropometricAssessment : MainAction
    data object FinishAssessment : MainAction
    data object NavigateBack : MainAction
    data object NavigateBackHome : MainAction
}

class MainViewModel : ViewModel() {
    var uiState by mutableStateOf(MainUiState())
        private set

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.NavigateTo -> {
                val selectedPatientId = if (
                    action.screen == NutryScreen.NUTRITIONAL_ANAMNESIS ||
                    action.screen == NutryScreen.ANTHROPOMETRIC_ASSESSMENT ||
                    action.screen == NutryScreen.ASSESSMENT_FINAL ||
                    action.screen == NutryScreen.PATIENT_HISTORY
                ) {
                    uiState.selectedPatientId
                } else {
                    null
                }
                pushEntry(
                    NavigationEntry(
                        screen = action.screen,
                        selectedPatientId = selectedPatientId
                    )
                )
            }

            is MainAction.StartPatientAssessment -> {
                pushEntry(
                    NavigationEntry(
                        screen = NutryScreen.NUTRITIONAL_ANAMNESIS,
                        selectedPatientId = action.patientId
                    )
                )
            }

            is MainAction.ViewPatientHistory -> {
                pushEntry(
                    NavigationEntry(
                        screen = NutryScreen.PATIENT_HISTORY,
                        selectedPatientId = action.patientId
                    )
                )
            }

            MainAction.ContinueToAnthropometricAssessment -> {
                pushEntry(
                    NavigationEntry(
                        screen = NutryScreen.ANTHROPOMETRIC_ASSESSMENT,
                        selectedPatientId = uiState.selectedPatientId
                    )
                )
            }

            MainAction.FinishAssessment -> {
                pushEntry(
                    NavigationEntry(
                        screen = NutryScreen.ASSESSMENT_FINAL,
                        selectedPatientId = uiState.selectedPatientId
                    )
                )
            }

            MainAction.NavigateBack -> {
                if (uiState.backStack.size <= 1) return
                val newStack = uiState.backStack.dropLast(1)
                val last = newStack.last()
                uiState = uiState.copy(
                    backStack = newStack,
                    currentScreen = last.screen,
                    selectedPatientId = last.selectedPatientId
                )
            }

            MainAction.NavigateBackHome -> {
                uiState = uiState.copy(
                    currentScreen = NutryScreen.HOME,
                    selectedPatientId = null,
                    backStack = listOf(
                        NavigationEntry(screen = NutryScreen.HOME, selectedPatientId = null)
                    )
                )
            }
        }
    }

    private fun pushEntry(entry: NavigationEntry) {
        val newStack = uiState.backStack + entry
        uiState = uiState.copy(
            backStack = newStack,
            currentScreen = entry.screen,
            selectedPatientId = entry.selectedPatientId
        )
    }
}
