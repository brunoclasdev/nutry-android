package com.bclas.nutry.core.di

import com.bclas.nutry.data.repository.InMemoryPatientRepository
import com.bclas.nutry.domain.repository.PatientRepository
import com.bclas.nutry.domain.usecase.GetPatientByIdUseCase
import com.bclas.nutry.domain.usecase.GetPatientsUseCase
import com.bclas.nutry.domain.usecase.SaveAssessmentForPatientUseCase
import com.bclas.nutry.domain.usecase.SavePatientUseCase

object AppContainer {
    private val patientRepository: PatientRepository = InMemoryPatientRepository()

    val getPatientsUseCase = GetPatientsUseCase(patientRepository)
    val getPatientByIdUseCase = GetPatientByIdUseCase(patientRepository)
    val savePatientUseCase = SavePatientUseCase(patientRepository)
    val saveAssessmentForPatientUseCase = SaveAssessmentForPatientUseCase(patientRepository)
}
