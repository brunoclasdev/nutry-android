package com.bclas.nutry.data.repository

import com.bclas.nutry.domain.model.AnthropometricData
import com.bclas.nutry.domain.model.AttendanceHistoryItem
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

    override fun updatePatient(patientId: Long, draft: PatientDraft): Result<Patient> {
        val index = patients.indexOfFirst { it.id == patientId }
        if (index == -1) {
            return Result.failure(IllegalStateException("Paciente nao encontrado."))
        }

        val name = draft.fullName.trim()
        if (name.isBlank()) {
            return Result.failure(IllegalArgumentException("Informe o nome completo."))
        }

        val updated = patients[index].copy(
            fullName = name,
            sex = draft.sex.trim(),
            birthDate = draft.birthDate.trim(),
            phone = draft.phone.trim(),
            email = draft.email.trim(),
            observations = draft.observations.trim(),
            patientPhoto = draft.patientPhoto.trim(),
            attendanceHistory = draft.attendanceHistory.mapIndexed { idx, item ->
                AttendanceHistoryItem(id = idx + 1L, description = item.description.trim())
            }
        )
        patients[index] = updated
        return Result.success(updated)
    }

    override fun deletePatient(patientId: Long): Result<Unit> {
        val removed = patients.removeIf { it.id == patientId }
        return if (removed) Result.success(Unit) else Result.failure(
            IllegalStateException("Paciente nao encontrado.")
        )
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

    override fun deleteAssessment(patientId: Long, assessmentId: Long): Result<Unit> {
        val index = patients.indexOfFirst { it.id == patientId }
        if (index == -1) {
            return Result.failure(IllegalStateException("Paciente nao encontrado."))
        }

        val updatedHistory = patients[index].assessmentHistory.filterNot { it.id == assessmentId }
        if (updatedHistory.size == patients[index].assessmentHistory.size) {
            return Result.failure(IllegalStateException("Avaliacao nao encontrada."))
        }

        patients[index] = patients[index].copy(assessmentHistory = updatedHistory)
        return Result.success(Unit)
    }
}

private fun nowFormatted(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date())
}
