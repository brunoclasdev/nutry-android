package com.bclas.nutry.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.Locale

data class ProtocolFieldUiState(
    val id: Long,
    val name: String,
    val value: String
)

data class AnthropometricAssessmentUiState(
    val weight: String = "",
    val height: String = "",
    val bmi: String = "",
    val abdominalCircumference: String = "",
    val waistCircumference: String = "",
    val hipCircumference: String = "",
    val waistHipRatio: String = "",
    val skinfolds: String = "",
    val bodyFatPercentage: String = "",
    val leanMass: String = "",
    val fatMass: String = "",
    val basalMetabolicRate: String = "",
    val totalEnergyExpenditure: String = "",
    val bodyWater: String = "",
    val protocolFields: List<ProtocolFieldUiState> = emptyList()
)

sealed interface AnthropometricAssessmentAction {
    data class WeightChanged(val value: String) : AnthropometricAssessmentAction
    data class HeightChanged(val value: String) : AnthropometricAssessmentAction
    data class AbdominalCircumferenceChanged(val value: String) : AnthropometricAssessmentAction
    data class WaistCircumferenceChanged(val value: String) : AnthropometricAssessmentAction
    data class HipCircumferenceChanged(val value: String) : AnthropometricAssessmentAction
    data class SkinfoldsChanged(val value: String) : AnthropometricAssessmentAction
    data class BodyFatPercentageChanged(val value: String) : AnthropometricAssessmentAction
    data class LeanMassChanged(val value: String) : AnthropometricAssessmentAction
    data class FatMassChanged(val value: String) : AnthropometricAssessmentAction
    data class BasalMetabolicRateChanged(val value: String) : AnthropometricAssessmentAction
    data class TotalEnergyExpenditureChanged(val value: String) : AnthropometricAssessmentAction
    data class BodyWaterChanged(val value: String) : AnthropometricAssessmentAction
    data class AddProtocolField(val fieldName: String) : AnthropometricAssessmentAction
    data class ProtocolFieldValueChanged(val fieldId: Long, val value: String) : AnthropometricAssessmentAction
    data class RemoveProtocolField(val fieldId: Long) : AnthropometricAssessmentAction
}

class AnthropometricAssessmentViewModel : ViewModel() {
    var uiState by mutableStateOf(AnthropometricAssessmentUiState())
        private set

    private var protocolFieldIdCounter = 1L

    fun onAction(action: AnthropometricAssessmentAction) {
        when (action) {
            is AnthropometricAssessmentAction.WeightChanged -> {
                updateAndRecalculate(uiState.copy(weight = action.value))
            }

            is AnthropometricAssessmentAction.HeightChanged -> {
                updateAndRecalculate(uiState.copy(height = action.value))
            }

            is AnthropometricAssessmentAction.AbdominalCircumferenceChanged -> {
                uiState = uiState.copy(abdominalCircumference = action.value)
            }

            is AnthropometricAssessmentAction.WaistCircumferenceChanged -> {
                updateAndRecalculate(uiState.copy(waistCircumference = action.value))
            }

            is AnthropometricAssessmentAction.HipCircumferenceChanged -> {
                updateAndRecalculate(uiState.copy(hipCircumference = action.value))
            }

            is AnthropometricAssessmentAction.SkinfoldsChanged -> {
                uiState = uiState.copy(skinfolds = action.value)
            }

            is AnthropometricAssessmentAction.BodyFatPercentageChanged -> {
                uiState = uiState.copy(bodyFatPercentage = action.value)
            }

            is AnthropometricAssessmentAction.LeanMassChanged -> {
                uiState = uiState.copy(leanMass = action.value)
            }

            is AnthropometricAssessmentAction.FatMassChanged -> {
                uiState = uiState.copy(fatMass = action.value)
            }

            is AnthropometricAssessmentAction.BasalMetabolicRateChanged -> {
                uiState = uiState.copy(basalMetabolicRate = action.value)
            }

            is AnthropometricAssessmentAction.TotalEnergyExpenditureChanged -> {
                uiState = uiState.copy(totalEnergyExpenditure = action.value)
            }

            is AnthropometricAssessmentAction.BodyWaterChanged -> {
                uiState = uiState.copy(bodyWater = action.value)
            }

            is AnthropometricAssessmentAction.AddProtocolField -> {
                val trimmedName = action.fieldName.trim()
                if (trimmedName.isBlank()) return
                uiState = uiState.copy(
                    protocolFields = uiState.protocolFields + ProtocolFieldUiState(
                        id = protocolFieldIdCounter++,
                        name = trimmedName,
                        value = ""
                    )
                )
            }

            is AnthropometricAssessmentAction.ProtocolFieldValueChanged -> {
                uiState = uiState.copy(
                    protocolFields = uiState.protocolFields.map { field ->
                        if (field.id == action.fieldId) field.copy(value = action.value) else field
                    }
                )
            }

            is AnthropometricAssessmentAction.RemoveProtocolField -> {
                uiState = uiState.copy(
                    protocolFields = uiState.protocolFields.filterNot { it.id == action.fieldId }
                )
            }
        }
    }

    private fun updateAndRecalculate(newState: AnthropometricAssessmentUiState) {
        uiState = newState.copy(
            bmi = calculateBmi(newState.weight, newState.height),
            waistHipRatio = calculateWaistHipRatio(newState.waistCircumference, newState.hipCircumference)
        )
    }

    private fun calculateBmi(weight: String, height: String): String {
        val weightValue = weight.parseDecimal() ?: return ""
        val heightValue = height.parseDecimal()?.let { if (it > 3.0) it / 100.0 else it } ?: return ""
        if (weightValue <= 0 || heightValue <= 0) return ""
        return ((weightValue / (heightValue * heightValue))).formatTwoDecimals()
    }

    private fun calculateWaistHipRatio(waist: String, hip: String): String {
        val waistValue = waist.parseDecimal() ?: return ""
        val hipValue = hip.parseDecimal() ?: return ""
        if (waistValue <= 0 || hipValue <= 0) return ""
        return (waistValue / hipValue).formatTwoDecimals()
    }
}

private fun String.parseDecimal(): Double? = replace(",", ".").toDoubleOrNull()

private fun Double.formatTwoDecimals(): String = String.format(Locale.US, "%.2f", this)
