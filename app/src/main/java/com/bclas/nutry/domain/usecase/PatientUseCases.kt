package com.bclas.nutry.domain.usecase

import com.bclas.nutry.domain.model.AnthropometricData
import com.bclas.nutry.domain.model.NutritionalAnamnesisData
import com.bclas.nutry.domain.model.Patient
import com.bclas.nutry.domain.model.PatientDraft
import com.bclas.nutry.domain.repository.PatientRepository

class GetPatientsUseCase(private val repository: PatientRepository) {
    operator fun invoke(): List<Patient> = repository.getPatients()
}

class GetPatientByIdUseCase(private val repository: PatientRepository) {
    operator fun invoke(patientId: Long): Patient? = repository.getPatientById(patientId)
}

class SavePatientUseCase(private val repository: PatientRepository) {
    operator fun invoke(draft: PatientDraft): Result<Patient> = repository.savePatient(draft)
}

class SaveAssessmentForPatientUseCase(private val repository: PatientRepository) {
    operator fun invoke(
        patientId: Long,
        anamnesis: NutritionalAnamnesisData,
        anthropometric: AnthropometricData
    ): Result<Unit> = repository.saveAssessmentForPatient(patientId, anamnesis, anthropometric)
}
