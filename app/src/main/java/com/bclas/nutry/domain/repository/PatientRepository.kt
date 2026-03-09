package com.bclas.nutry.domain.repository

import com.bclas.nutry.domain.model.AnthropometricData
import com.bclas.nutry.domain.model.NutritionalAnamnesisData
import com.bclas.nutry.domain.model.Patient
import com.bclas.nutry.domain.model.PatientDraft

interface PatientRepository {
    fun getPatients(): List<Patient>
    fun getPatientById(patientId: Long): Patient?
    fun savePatient(draft: PatientDraft): Result<Patient>
    fun saveAssessmentForPatient(
        patientId: Long,
        anamnesis: NutritionalAnamnesisData,
        anthropometric: AnthropometricData
    ): Result<Unit>
}
