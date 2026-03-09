package com.bclas.nutry.data.repository

import com.bclas.nutry.data.local.AssessmentEntity
import com.bclas.nutry.data.local.PatientDao
import com.bclas.nutry.data.local.PatientEntity
import com.bclas.nutry.domain.model.AnthropometricData
import com.bclas.nutry.domain.model.AttendanceHistoryItem
import com.bclas.nutry.domain.model.NutritionalAnamnesisData
import com.bclas.nutry.domain.model.Patient
import com.bclas.nutry.domain.model.PatientAssessmentRecord
import com.bclas.nutry.domain.model.PatientDraft
import com.bclas.nutry.domain.repository.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RoomPatientRepository(
    private val patientDao: PatientDao
) : PatientRepository {

    override fun getPatients(): List<Patient> = runBlocking {
        withContext(Dispatchers.IO) {
            val patients = patientDao.getPatients()
            val assessments = patientDao.getAllAssessments().groupBy { it.patientId }
            patients.map { entity ->
                entity.toDomain(
                    assessments = assessments[entity.id].orEmpty()
                )
            }
        }
    }

    override fun getPatientById(patientId: Long): Patient? = runBlocking {
        withContext(Dispatchers.IO) {
            val patient = patientDao.getPatientById(patientId) ?: return@withContext null
            val assessments = patientDao.getAssessmentsByPatientId(patient.id)
            patient.toDomain(assessments)
        }
    }

    override fun savePatient(draft: PatientDraft): Result<Patient> = runBlocking {
        withContext(Dispatchers.IO) {
            val name = draft.fullName.trim()
            if (name.isBlank()) {
                return@withContext Result.failure(IllegalArgumentException("Informe o nome completo."))
            }

            val patientEntity = PatientEntity(
                fullName = name,
                sex = draft.sex.trim(),
                birthDate = draft.birthDate.trim(),
                phone = draft.phone.trim(),
                email = draft.email.trim(),
                observations = draft.observations.trim(),
                patientPhoto = draft.patientPhoto.trim(),
                attendanceHistorySerialized = draft.attendanceHistory.joinToString("\n") {
                    it.description.trim()
                }
            )

            val patientId = patientDao.insertPatient(patientEntity)
            val persisted = patientDao.getPatientById(patientId)
                ?: return@withContext Result.failure(IllegalStateException("Falha ao salvar paciente."))

            Result.success(persisted.toDomain(emptyList()))
        }
    }

    override fun saveAssessmentForPatient(
        patientId: Long,
        anamnesis: NutritionalAnamnesisData,
        anthropometric: AnthropometricData
    ): Result<Unit> = runBlocking {
        withContext(Dispatchers.IO) {
            val patient = patientDao.getPatientById(patientId)
                ?: return@withContext Result.failure(IllegalStateException("Paciente nao encontrado."))

            val assessmentEntity = AssessmentEntity(
                patientId = patient.id,
                createdAt = nowFormatted(),
                anamnesisPatientGoal = anamnesis.patientGoal,
                anamnesisDietaryRoutine = anamnesis.dietaryRoutine,
                anamnesisDietaryPreferences = anamnesis.dietaryPreferences,
                anamnesisRestrictions = anamnesis.restrictions,
                anamnesisIntolerancesAndAllergies = anamnesis.intolerancesAndAllergies,
                anamnesisPathologies = anamnesis.pathologies,
                anamnesisMedicationUse = anamnesis.medicationUse,
                anamnesisSupplementation = anamnesis.supplementation,
                anamnesisWaterIntake = anamnesis.waterIntake,
                anamnesisSleep = anamnesis.sleep,
                anamnesisIntestinalFrequency = anamnesis.intestinalFrequency,
                anamnesisPhysicalActivityLevel = anamnesis.physicalActivityLevel,
                anthropometricWeight = anthropometric.weight,
                anthropometricHeight = anthropometric.height,
                anthropometricAge = anthropometric.age,
                anthropometricBiologicalSex = anthropometric.biologicalSex,
                anthropometricActivityLevel = anthropometric.activityLevel,
                anthropometricBmi = anthropometric.bmi,
                anthropometricAbdominalCircumference = anthropometric.abdominalCircumference,
                anthropometricWaistCircumference = anthropometric.waistCircumference,
                anthropometricHipCircumference = anthropometric.hipCircumference,
                anthropometricWaistHipRatio = anthropometric.waistHipRatio,
                anthropometricSkinfolds = anthropometric.skinfolds,
                anthropometricBodyFatPercentage = anthropometric.bodyFatPercentage,
                anthropometricLeanMass = anthropometric.leanMass,
                anthropometricFatMass = anthropometric.fatMass,
                anthropometricBasalMetabolicRate = anthropometric.basalMetabolicRate,
                anthropometricTotalEnergyExpenditure = anthropometric.totalEnergyExpenditure,
                anthropometricBodyWater = anthropometric.bodyWater
            )

            patientDao.insertAssessment(assessmentEntity)
            val newAttendanceEntry = buildString {
                append(nowFormatted())
                append(" - Avaliacao concluida")
                if (anthropometric.bmi.isNotBlank()) {
                    append(" (IMC: ")
                    append(anthropometric.bmi)
                    append(")")
                }
            }
            val updatedAttendanceHistory = listOfNotNull(
                patient.attendanceHistorySerialized.takeIf { it.isNotBlank() },
                newAttendanceEntry
            ).joinToString("\n")
            patientDao.updateAttendanceHistory(patient.id, updatedAttendanceHistory)
            Result.success(Unit)
        }
    }
}

private fun PatientEntity.toDomain(assessments: List<AssessmentEntity>): Patient {
    return Patient(
        id = id,
        fullName = fullName,
        sex = sex,
        birthDate = birthDate,
        phone = phone,
        email = email,
        observations = observations,
        patientPhoto = patientPhoto,
        attendanceHistory = attendanceHistorySerialized
            .split("\n")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .mapIndexed { index, description ->
                AttendanceHistoryItem(id = index + 1L, description = description)
            },
        assessmentHistory = assessments.map { it.toDomain() }
    )
}

private fun AssessmentEntity.toDomain(): PatientAssessmentRecord {
    return PatientAssessmentRecord(
        id = id,
        createdAt = createdAt,
        anamnesis = NutritionalAnamnesisData(
            patientGoal = anamnesisPatientGoal,
            dietaryRoutine = anamnesisDietaryRoutine,
            dietaryPreferences = anamnesisDietaryPreferences,
            restrictions = anamnesisRestrictions,
            intolerancesAndAllergies = anamnesisIntolerancesAndAllergies,
            pathologies = anamnesisPathologies,
            medicationUse = anamnesisMedicationUse,
            supplementation = anamnesisSupplementation,
            waterIntake = anamnesisWaterIntake,
            sleep = anamnesisSleep,
            intestinalFrequency = anamnesisIntestinalFrequency,
            physicalActivityLevel = anamnesisPhysicalActivityLevel
        ),
        anthropometric = AnthropometricData(
            weight = anthropometricWeight,
            height = anthropometricHeight,
            age = anthropometricAge,
            biologicalSex = anthropometricBiologicalSex,
            activityLevel = anthropometricActivityLevel,
            bmi = anthropometricBmi,
            abdominalCircumference = anthropometricAbdominalCircumference,
            waistCircumference = anthropometricWaistCircumference,
            hipCircumference = anthropometricHipCircumference,
            waistHipRatio = anthropometricWaistHipRatio,
            skinfolds = anthropometricSkinfolds,
            bodyFatPercentage = anthropometricBodyFatPercentage,
            leanMass = anthropometricLeanMass,
            fatMass = anthropometricFatMass,
            basalMetabolicRate = anthropometricBasalMetabolicRate,
            totalEnergyExpenditure = anthropometricTotalEnergyExpenditure,
            bodyWater = anthropometricBodyWater
        )
    )
}

private fun nowFormatted(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date())
}
