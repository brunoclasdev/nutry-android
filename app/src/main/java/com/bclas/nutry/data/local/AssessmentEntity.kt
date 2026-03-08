package com.bclas.nutry.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "assessments",
    foreignKeys = [
        ForeignKey(
            entity = PatientEntity::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["patientId"])]
)
data class AssessmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val patientId: Long,
    val createdAt: String,
    val anamnesisPatientGoal: String,
    val anamnesisDietaryRoutine: String,
    val anamnesisDietaryPreferences: String,
    val anamnesisRestrictions: String,
    val anamnesisIntolerancesAndAllergies: String,
    val anamnesisPathologies: String,
    val anamnesisMedicationUse: String,
    val anamnesisSupplementation: String,
    val anamnesisWaterIntake: String,
    val anamnesisSleep: String,
    val anamnesisIntestinalFrequency: String,
    val anamnesisPhysicalActivityLevel: String,
    val anthropometricWeight: String,
    val anthropometricHeight: String,
    val anthropometricAge: String,
    val anthropometricBiologicalSex: String,
    val anthropometricActivityLevel: String,
    val anthropometricBmi: String,
    val anthropometricAbdominalCircumference: String,
    val anthropometricWaistCircumference: String,
    val anthropometricHipCircumference: String,
    val anthropometricWaistHipRatio: String,
    val anthropometricSkinfolds: String,
    val anthropometricBodyFatPercentage: String,
    val anthropometricLeanMass: String,
    val anthropometricFatMass: String,
    val anthropometricBasalMetabolicRate: String,
    val anthropometricTotalEnergyExpenditure: String,
    val anthropometricBodyWater: String
)
