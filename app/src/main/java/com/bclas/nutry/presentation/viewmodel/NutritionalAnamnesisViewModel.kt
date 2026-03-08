package com.bclas.nutry.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class NutritionalAnamnesisUiState(
    val patientGoal: String = "",
    val dietaryRoutine: String = "",
    val dietaryPreferences: String = "",
    val restrictions: String = "",
    val intolerancesAndAllergies: String = "",
    val pathologies: String = "",
    val medicationUse: String = "",
    val supplementation: String = "",
    val waterIntake: String = "",
    val sleep: String = "",
    val intestinalFrequency: String = "",
    val physicalActivityLevel: String = "",
    val feedbackMessage: String? = null,
    val feedbackSuccess: Boolean = false
)

sealed interface NutritionalAnamnesisAction {
    data class PatientGoalChanged(val value: String) : NutritionalAnamnesisAction
    data class DietaryRoutineChanged(val value: String) : NutritionalAnamnesisAction
    data class DietaryPreferencesChanged(val value: String) : NutritionalAnamnesisAction
    data class RestrictionsChanged(val value: String) : NutritionalAnamnesisAction
    data class IntolerancesAndAllergiesChanged(val value: String) : NutritionalAnamnesisAction
    data class PathologiesChanged(val value: String) : NutritionalAnamnesisAction
    data class MedicationUseChanged(val value: String) : NutritionalAnamnesisAction
    data class SupplementationChanged(val value: String) : NutritionalAnamnesisAction
    data class WaterIntakeChanged(val value: String) : NutritionalAnamnesisAction
    data class SleepChanged(val value: String) : NutritionalAnamnesisAction
    data class IntestinalFrequencyChanged(val value: String) : NutritionalAnamnesisAction
    data class PhysicalActivityLevelChanged(val value: String) : NutritionalAnamnesisAction
    data object SaveAnamnesis : NutritionalAnamnesisAction
    data object CancelAnamnesis : NutritionalAnamnesisAction
    data object DismissFeedback : NutritionalAnamnesisAction
}

class NutritionalAnamnesisViewModel : ViewModel() {
    var uiState by mutableStateOf(NutritionalAnamnesisUiState())
        private set

    fun onAction(action: NutritionalAnamnesisAction) {
        when (action) {
            is NutritionalAnamnesisAction.PatientGoalChanged -> {
                uiState = uiState.copy(patientGoal = action.value)
            }

            is NutritionalAnamnesisAction.DietaryRoutineChanged -> {
                uiState = uiState.copy(dietaryRoutine = action.value)
            }

            is NutritionalAnamnesisAction.DietaryPreferencesChanged -> {
                uiState = uiState.copy(dietaryPreferences = action.value)
            }

            is NutritionalAnamnesisAction.RestrictionsChanged -> {
                uiState = uiState.copy(restrictions = action.value)
            }

            is NutritionalAnamnesisAction.IntolerancesAndAllergiesChanged -> {
                uiState = uiState.copy(intolerancesAndAllergies = action.value)
            }

            is NutritionalAnamnesisAction.PathologiesChanged -> {
                uiState = uiState.copy(pathologies = action.value)
            }

            is NutritionalAnamnesisAction.MedicationUseChanged -> {
                uiState = uiState.copy(medicationUse = action.value)
            }

            is NutritionalAnamnesisAction.SupplementationChanged -> {
                uiState = uiState.copy(supplementation = action.value)
            }

            is NutritionalAnamnesisAction.WaterIntakeChanged -> {
                uiState = uiState.copy(waterIntake = action.value)
            }

            is NutritionalAnamnesisAction.SleepChanged -> {
                uiState = uiState.copy(sleep = action.value)
            }

            is NutritionalAnamnesisAction.IntestinalFrequencyChanged -> {
                uiState = uiState.copy(intestinalFrequency = action.value)
            }

            is NutritionalAnamnesisAction.PhysicalActivityLevelChanged -> {
                uiState = uiState.copy(physicalActivityLevel = action.value)
            }

            NutritionalAnamnesisAction.SaveAnamnesis -> {
                if (uiState.patientGoal.isBlank() && uiState.dietaryRoutine.isBlank()) {
                    uiState = uiState.copy(
                        feedbackMessage = "Nao foi possivel salvar. Preencha objetivo ou rotina alimentar.",
                        feedbackSuccess = false
                    )
                } else {
                    uiState = uiState.copy(
                        feedbackMessage = "Anamnese nutricional salva com sucesso.",
                        feedbackSuccess = true
                    )
                }
            }

            NutritionalAnamnesisAction.CancelAnamnesis -> {
                uiState = NutritionalAnamnesisUiState(
                    feedbackMessage = "Anamnese cancelada e formulario limpo.",
                    feedbackSuccess = true
                )
            }

            NutritionalAnamnesisAction.DismissFeedback -> {
                uiState = uiState.copy(feedbackMessage = null)
            }
        }
    }
}
