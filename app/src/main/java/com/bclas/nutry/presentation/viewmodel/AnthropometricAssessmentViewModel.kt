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
    val age: String = "",
    val biologicalSex: String = "",
    val activityLevel: String = "Sedentario",
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
    val protocolFields: List<ProtocolFieldUiState> = emptyList(),
    val feedbackMessage: String? = null,
    val feedbackSuccess: Boolean = false
)

sealed interface AnthropometricAssessmentAction {
    data class WeightChanged(val value: String) : AnthropometricAssessmentAction
    data class HeightChanged(val value: String) : AnthropometricAssessmentAction
    data class AgeChanged(val value: String) : AnthropometricAssessmentAction
    data class BiologicalSexChanged(val value: String) : AnthropometricAssessmentAction
    data class ActivityLevelChanged(val value: String) : AnthropometricAssessmentAction
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
    data object SaveAssessment : AnthropometricAssessmentAction
    data object CancelAssessment : AnthropometricAssessmentAction
    data object ClearForm : AnthropometricAssessmentAction
    data object DismissFeedback : AnthropometricAssessmentAction
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

            is AnthropometricAssessmentAction.AgeChanged -> {
                updateAndRecalculate(uiState.copy(age = action.value))
            }

            is AnthropometricAssessmentAction.BiologicalSexChanged -> {
                updateAndRecalculate(uiState.copy(biologicalSex = action.value))
            }

            is AnthropometricAssessmentAction.ActivityLevelChanged -> {
                updateAndRecalculate(uiState.copy(activityLevel = action.value))
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
                updateAndRecalculate(uiState.copy(skinfolds = action.value))
            }

            is AnthropometricAssessmentAction.BodyFatPercentageChanged -> {
                updateAndRecalculate(uiState.copy(bodyFatPercentage = action.value))
            }

            is AnthropometricAssessmentAction.LeanMassChanged -> {
                updateAndRecalculate(uiState.copy(leanMass = action.value))
            }

            is AnthropometricAssessmentAction.FatMassChanged -> {
                updateAndRecalculate(uiState.copy(fatMass = action.value))
            }

            is AnthropometricAssessmentAction.BasalMetabolicRateChanged -> {
                updateAndRecalculate(uiState.copy(basalMetabolicRate = action.value))
            }

            is AnthropometricAssessmentAction.TotalEnergyExpenditureChanged -> {
                updateAndRecalculate(uiState.copy(totalEnergyExpenditure = action.value))
            }

            is AnthropometricAssessmentAction.BodyWaterChanged -> {
                updateAndRecalculate(uiState.copy(bodyWater = action.value))
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

            AnthropometricAssessmentAction.SaveAssessment -> {
                if (uiState.weight.isBlank() || uiState.height.isBlank()) {
                    uiState = uiState.copy(
                        feedbackMessage = "Nao foi possivel salvar. Preencha pelo menos peso e altura.",
                        feedbackSuccess = false
                    )
                } else {
                    uiState = uiState.copy(
                        feedbackMessage = "Avaliacao antropometrica salva com sucesso.",
                        feedbackSuccess = true
                    )
                }
            }

            AnthropometricAssessmentAction.CancelAssessment -> {
                uiState = AnthropometricAssessmentUiState(
                    feedbackMessage = "Avaliacao cancelada e formulario limpo.",
                    feedbackSuccess = true
                )
            }

            AnthropometricAssessmentAction.ClearForm -> {
                uiState = AnthropometricAssessmentUiState()
            }

            AnthropometricAssessmentAction.DismissFeedback -> {
                uiState = uiState.copy(feedbackMessage = null)
            }
        }
    }

    private fun updateAndRecalculate(newState: AnthropometricAssessmentUiState) {
        val bodyFat = calculateBodyFatPercentage(
            manualBodyFat = newState.bodyFatPercentage,
            skinfolds = newState.skinfolds,
            age = newState.age,
            sex = newState.biologicalSex
        )
        val fatMass = calculateFatMass(newState.weight, bodyFat)
        val leanMass = calculateLeanMass(newState.weight, fatMass)
        val bmr = calculateBasalMetabolicRate(
            weight = newState.weight,
            height = newState.height,
            age = newState.age,
            sex = newState.biologicalSex,
            leanMass = leanMass
        )
        val tee = calculateTotalEnergyExpenditure(bmr, newState.activityLevel)
        val bodyWater = calculateBodyWater(bodyFat)

        uiState = newState.copy(
            bmi = calculateBmi(newState.weight, newState.height),
            waistHipRatio = calculateWaistHipRatio(newState.waistCircumference, newState.hipCircumference),
            bodyFatPercentage = bodyFat,
            fatMass = fatMass,
            leanMass = leanMass,
            basalMetabolicRate = bmr,
            totalEnergyExpenditure = tee,
            bodyWater = bodyWater
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

    private fun calculateBodyFatPercentage(
        manualBodyFat: String,
        skinfolds: String,
        age: String,
        sex: String
    ): String {
        val folds = skinfolds.parseDecimal()
        val ageValue = age.toIntOrNull()
        if (folds != null && ageValue != null && folds > 0 && ageValue > 0) {
            val density = when (sex.lowercase(Locale.ROOT)) {
                "masculino" -> 1.10938 - (0.0008267 * folds) + (0.0000016 * folds * folds) - (0.0002574 * ageValue)
                "feminino" -> 1.0994921 - (0.0009929 * folds) + (0.0000023 * folds * folds) - (0.0001392 * ageValue)
                else -> return ""
            }
            if (density <= 0.0) return ""
            return ((495.0 / density) - 450.0).formatTwoDecimals()
        }

        val manual = manualBodyFat.parseDecimal() ?: return ""
        if (manual <= 0) return ""
        return manual.formatTwoDecimals()
    }

    private fun calculateFatMass(weight: String, bodyFatPercentage: String): String {
        val weightValue = weight.parseDecimal() ?: return ""
        val bodyFatValue = bodyFatPercentage.parseDecimal() ?: return ""
        if (weightValue <= 0 || bodyFatValue <= 0) return ""
        return (weightValue * (bodyFatValue / 100.0)).formatTwoDecimals()
    }

    private fun calculateLeanMass(weight: String, fatMass: String): String {
        val weightValue = weight.parseDecimal() ?: return ""
        val fatMassValue = fatMass.parseDecimal() ?: return ""
        if (weightValue <= 0 || fatMassValue < 0 || fatMassValue > weightValue) return ""
        return (weightValue - fatMassValue).formatTwoDecimals()
    }

    private fun calculateBasalMetabolicRate(
        weight: String,
        height: String,
        age: String,
        sex: String,
        leanMass: String
    ): String {
        val leanMassValue = leanMass.parseDecimal()
        if (leanMassValue != null && leanMassValue > 0) {
            return (370.0 + (21.6 * leanMassValue)).formatTwoDecimals()
        }

        val weightValue = weight.parseDecimal() ?: return ""
        val heightValue = height.parseDecimal()?.let { if (it > 3.0) it else it * 100.0 } ?: return ""
        val ageValue = age.toIntOrNull() ?: return ""
        if (weightValue <= 0 || heightValue <= 0 || ageValue <= 0) return ""

        val bmr = when (sex.lowercase(Locale.ROOT)) {
            "masculino" -> (10 * weightValue) + (6.25 * heightValue) - (5 * ageValue) + 5
            "feminino" -> (10 * weightValue) + (6.25 * heightValue) - (5 * ageValue) - 161
            else -> return ""
        }
        return bmr.formatTwoDecimals()
    }

    private fun calculateTotalEnergyExpenditure(bmr: String, activityLevel: String): String {
        val bmrValue = bmr.parseDecimal() ?: return ""
        if (bmrValue <= 0) return ""
        val factor = when (activityLevel.lowercase(Locale.ROOT)) {
            "sedentario" -> 1.2
            "leve" -> 1.375
            "moderado" -> 1.55
            "intenso" -> 1.725
            "muito intenso" -> 1.9
            else -> 1.2
        }
        return (bmrValue * factor).formatTwoDecimals()
    }

    private fun calculateBodyWater(bodyFatPercentage: String): String {
        val bodyFatValue = bodyFatPercentage.parseDecimal() ?: return ""
        if (bodyFatValue <= 0 || bodyFatValue >= 100) return ""
        return ((1 - (bodyFatValue / 100.0)) * 73.0).formatTwoDecimals()
    }
}

private fun String.parseDecimal(): Double? = replace(",", ".").toDoubleOrNull()

private fun Double.formatTwoDecimals(): String = String.format(Locale.US, "%.2f", this)
