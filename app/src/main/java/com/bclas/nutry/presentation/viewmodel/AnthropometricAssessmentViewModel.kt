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
    val bmrFormula: String = "Harris-Benedict",
    val ventilationLMin: String = "",
    val maxBodyTemperatureC: String = "",
    val bmi: String = "",
    val idealWeight: String = "",
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
    data class BmrFormulaChanged(val value: String) : AnthropometricAssessmentAction
    data class VentilationLMinChanged(val value: String) : AnthropometricAssessmentAction
    data class MaxBodyTemperatureCChanged(val value: String) : AnthropometricAssessmentAction
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

            is AnthropometricAssessmentAction.BmrFormulaChanged -> {
                updateAndRecalculate(uiState.copy(bmrFormula = action.value))
            }

            is AnthropometricAssessmentAction.VentilationLMinChanged -> {
                updateAndRecalculate(uiState.copy(ventilationLMin = action.value))
            }

            is AnthropometricAssessmentAction.MaxBodyTemperatureCChanged -> {
                updateAndRecalculate(uiState.copy(maxBodyTemperatureC = action.value))
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
        val idealWeight = calculateIdealWeight(
            height = newState.height,
            sex = newState.biologicalSex
        )
        val fatMass = calculateFatMass(newState.weight, bodyFat)
        val leanMass = calculateLeanMass(newState.weight, fatMass)
        val bmr = calculateBasalMetabolicRate(
            weight = newState.weight,
            height = newState.height,
            age = newState.age,
            sex = newState.biologicalSex,
            leanMass = leanMass,
            formula = newState.bmrFormula,
            ventilationLMin = newState.ventilationLMin,
            maxBodyTemperatureC = newState.maxBodyTemperatureC
        )
        val tee = calculateTotalEnergyExpenditure(bmr, newState.activityLevel)
        val bodyWater = calculateBodyWater(bodyFat)

        uiState = newState.copy(
            bmi = calculateBmi(newState.weight, newState.height),
            idealWeight = idealWeight,
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
        leanMass: String,
        formula: String,
        ventilationLMin: String,
        maxBodyTemperatureC: String
    ): String {
        val weightValue = weight.parseDecimal() ?: return ""
        val heightCm = height.parseDecimal()?.let { if (it > 3.0) it else it * 100.0 } ?: return ""
        val ageValue = age.toIntOrNull() ?: return ""
        if (weightValue <= 0 || heightCm <= 0 || ageValue <= 0) return ""

        val leanMassValue = leanMass.parseDecimal()
        val isMale = sex.lowercase(Locale.ROOT) == "masculino"
        val isFemale = sex.lowercase(Locale.ROOT) == "feminino"
        if (!isMale && !isFemale) return ""

        val mifflin = if (isMale) {
            (10 * weightValue) + (6.25 * heightCm) - (5 * ageValue) + 5
        } else {
            (10 * weightValue) + (6.25 * heightCm) - (5 * ageValue) - 161
        }

        val bmr = when (formula) {
            "Harris-Benedict" -> {
                if (isMale) {
                    88.362 + (13.397 * weightValue) + (4.799 * heightCm) - (5.677 * ageValue)
                } else {
                    447.593 + (9.247 * weightValue) + (3.098 * heightCm) - (4.330 * ageValue)
                }
            }
            "Mifflin-St Jeor" -> mifflin
            "Katch-McArdle" -> {
                if (leanMassValue == null || leanMassValue <= 0) return ""
                370.0 + (21.6 * leanMassValue)
            }
            "Cunningham" -> {
                if (leanMassValue == null || leanMassValue <= 0) return ""
                500.0 + (22.0 * leanMassValue)
            }
            "Owen" -> {
                if (isMale) {
                    879.0 + (10.2 * weightValue)
                } else {
                    795.0 + (7.18 * weightValue)
                }
            }
            "WHO/FAO" -> calculateWhoFaoBmr(weightValue, ageValue, isMale) ?: return ""
            "Schofield" -> calculateSchofieldBmr(weightValue, ageValue, isMale) ?: return ""
            "Henry" -> calculateHenryBmr(weightValue, ageValue, isMale) ?: return ""
            "Ireton-Jones" -> {
                // Version for ventilated hospitalized patients (trauma/burn = 0 by default)
                1784.0 - (11.0 * ageValue) + (5.0 * weightValue) + if (isMale) 244.0 else 0.0
            }
            "Penn State" -> {
                val ventilation = ventilationLMin.parseDecimal() ?: return ""
                val maxTemp = maxBodyTemperatureC.parseDecimal() ?: return ""
                if (ventilation <= 0 || maxTemp <= 0) return ""
                (0.96 * mifflin) + (31.0 * ventilation) + (167.0 * maxTemp) - 6212.0
            }
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

    private fun calculateIdealWeight(height: String, sex: String): String {
        val heightCm = height.parseDecimal()?.let { if (it > 3.0) it else it * 100.0 } ?: return ""
        if (heightCm <= 0) return ""
        val heightInches = heightCm / 2.54
        val inchesOverFiveFeet = heightInches - 60.0
        val base = when (sex.lowercase(Locale.ROOT)) {
            "masculino" -> 50.0
            "feminino" -> 45.5
            else -> return ""
        }
        return (base + (2.3 * inchesOverFiveFeet)).formatTwoDecimals()
    }

    private fun calculateWhoFaoBmr(weight: Double, age: Int, male: Boolean): Double? {
        return if (male) {
            when {
                age < 18 -> null
                age < 30 -> 15.3 * weight + 679
                age < 60 -> 11.6 * weight + 879
                else -> 13.5 * weight + 487
            }
        } else {
            when {
                age < 18 -> null
                age < 30 -> 14.7 * weight + 496
                age < 60 -> 8.7 * weight + 829
                else -> 10.5 * weight + 596
            }
        }
    }

    private fun calculateSchofieldBmr(weight: Double, age: Int, male: Boolean): Double? {
        return if (male) {
            when {
                age < 18 -> null
                age < 30 -> 15.057 * weight + 692.2
                age < 60 -> 11.472 * weight + 873.1
                else -> 11.711 * weight + 587.7
            }
        } else {
            when {
                age < 18 -> null
                age < 30 -> 14.818 * weight + 486.6
                age < 60 -> 8.126 * weight + 845.6
                else -> 9.082 * weight + 658.5
            }
        }
    }

    private fun calculateHenryBmr(weight: Double, age: Int, male: Boolean): Double? {
        return if (male) {
            when {
                age < 18 -> null
                age < 30 -> 16.0 * weight + 545
                age < 60 -> 14.2 * weight + 593
                else -> 13.5 * weight + 514
            }
        } else {
            when {
                age < 18 -> null
                age < 30 -> 13.1 * weight + 558
                age < 60 -> 9.74 * weight + 694
                else -> 10.1 * weight + 569
            }
        }
    }
}

private fun String.parseDecimal(): Double? = replace(",", ".").toDoubleOrNull()

private fun Double.formatTwoDecimals(): String = String.format(Locale.US, "%.2f", this)

data class BmrFormulaOption(
    val name: String,
    val mainUse: String
)

val BMR_FORMULA_OPTIONS = listOf(
    BmrFormulaOption("Harris-Benedict", "tradicional"),
    BmrFormulaOption("Mifflin-St Jeor", "clinica moderna"),
    BmrFormulaOption("Katch-McArdle", "atletas"),
    BmrFormulaOption("Cunningham", "atletas"),
    BmrFormulaOption("Owen", "calculo simples"),
    BmrFormulaOption("WHO/FAO", "saude publica"),
    BmrFormulaOption("Schofield", "base cientifica"),
    BmrFormulaOption("Henry", "atualizacao da Schofield"),
    BmrFormulaOption("Ireton-Jones", "hospital"),
    BmrFormulaOption("Penn State", "UTI")
)
