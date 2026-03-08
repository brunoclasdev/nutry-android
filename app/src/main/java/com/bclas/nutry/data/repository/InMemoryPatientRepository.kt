package com.bclas.nutry.data.repository

import com.bclas.nutry.domain.model.AnthropometricData
import com.bclas.nutry.domain.model.NutritionalAnamnesisData
import com.bclas.nutry.domain.model.Patient
import com.bclas.nutry.domain.model.PatientAssessmentRecord
import com.bclas.nutry.domain.model.PatientDraft
import com.bclas.nutry.domain.repository.PatientRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InMemoryPatientRepository : PatientRepository {
    private val patients = mutableListOf<Patient>()
    private var patientIdCounter = 1L
    private var assessmentIdCounter = 1L

    override fun getPatients(): List<Patient> = patients.toList()

    override fun getPatientById(patientId: Long): Patient? = patients.firstOrNull { it.id == patientId }

    override fun savePatient(draft: PatientDraft): Result<Patient> {
        val name = draft.fullName.trim()
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Informe o nome completo."))
        }

        val patient = Patient(
            id = patientIdCounter++,
            fullName = name,
            sex = draft.sex.trim(),
            birthDate = draft.birthDate.trim(),
            phone = draft.phone.trim(),
            email = draft.email.trim(),
            observations = draft.observations.trim(),
            patientPhoto = draft.patientPhoto.trim(),
            attendanceHistory = draft.attendanceHistory,
            assessmentHistory = emptyList()
        )
        patients.add(patient)
        return Result.success(patient)
    }

    override fun saveAssessmentForPatient(
        patientId: Long,
        anamnesis: NutritionalAnamnesisData,
        anthropometric: AnthropometricData
    ): Result<Unit> {
        val index = patients.indexOfFirst { it.id == patientId }
        if (index == -1) {
            return Result.failure(IllegalStateException("Paciente nao encontrado."))
        }

        val record = PatientAssessmentRecord(
            id = assessmentIdCounter++,
            createdAt = nowFormatted(),
            anamnesis = anamnesis,
            anthropometric = anthropometric
        )

        patients[index] = patients[index].copy(
            assessmentHistory = patients[index].assessmentHistory + record
        )

        return Result.success(Unit)
    }
}

private fun nowFormatted(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date())
}
