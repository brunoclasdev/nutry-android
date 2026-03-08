package com.bclas.nutry.core.di

import android.content.Context
import com.bclas.nutry.data.local.NutryDatabase
import com.bclas.nutry.data.repository.RoomPatientRepository
import com.bclas.nutry.domain.repository.PatientRepository
import com.bclas.nutry.domain.usecase.GetPatientByIdUseCase
import com.bclas.nutry.domain.usecase.GetPatientsUseCase
import com.bclas.nutry.domain.usecase.SaveAssessmentForPatientUseCase
import com.bclas.nutry.domain.usecase.SavePatientUseCase

object AppContainer {
    private var isInitialized = false
    private lateinit var patientRepository: PatientRepository

    lateinit var getPatientsUseCase: GetPatientsUseCase
        private set
    lateinit var getPatientByIdUseCase: GetPatientByIdUseCase
        private set
    lateinit var savePatientUseCase: SavePatientUseCase
        private set
    lateinit var saveAssessmentForPatientUseCase: SaveAssessmentForPatientUseCase
        private set

    fun initialize(context: Context) {
        if (isInitialized) return

        val database = NutryDatabase.getInstance(context)
        patientRepository = RoomPatientRepository(database.patientDao())

        getPatientsUseCase = GetPatientsUseCase(patientRepository)
        getPatientByIdUseCase = GetPatientByIdUseCase(patientRepository)
        savePatientUseCase = SavePatientUseCase(patientRepository)
        saveAssessmentForPatientUseCase = SaveAssessmentForPatientUseCase(patientRepository)
        isInitialized = true
    }
}
