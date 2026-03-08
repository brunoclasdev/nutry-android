package com.bclas.nutry.domain.model

data class AttendanceHistoryItem(
    val id: Long,
    val description: String
)

data class NutritionalAnamnesisData(
    val patientGoal: String,
    val dietaryRoutine: String,
    val dietaryPreferences: String,
    val restrictions: String,
    val intolerancesAndAllergies: String,
    val pathologies: String,
    val medicationUse: String,
    val supplementation: String,
    val waterIntake: String,
    val sleep: String,
    val intestinalFrequency: String,
    val physicalActivityLevel: String
)

data class AnthropometricData(
    val weight: String,
    val height: String,
    val age: String,
    val biologicalSex: String,
    val activityLevel: String,
    val bmi: String,
    val abdominalCircumference: String,
    val waistCircumference: String,
    val hipCircumference: String,
    val waistHipRatio: String,
    val skinfolds: String,
    val bodyFatPercentage: String,
    val leanMass: String,
    val fatMass: String,
    val basalMetabolicRate: String,
    val totalEnergyExpenditure: String,
    val bodyWater: String
)

data class PatientAssessmentRecord(
    val id: Long,
    val createdAt: String,
    val anamnesis: NutritionalAnamnesisData,
    val anthropometric: AnthropometricData
)

data class Patient(
    val id: Long,
    val fullName: String,
    val sex: String,
    val birthDate: String,
    val phone: String,
    val email: String,
    val observations: String,
    val patientPhoto: String,
    val attendanceHistory: List<AttendanceHistoryItem>,
    val assessmentHistory: List<PatientAssessmentRecord>
)

data class PatientDraft(
    val fullName: String,
    val sex: String,
    val birthDate: String,
    val phone: String,
    val email: String,
    val observations: String,
    val patientPhoto: String,
    val attendanceHistory: List<AttendanceHistoryItem>
)
