package com.bclas.nutry.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PatientDao {
    @Insert
    suspend fun insertPatient(patient: PatientEntity): Long

    @Query("SELECT * FROM patients ORDER BY id ASC")
    suspend fun getPatients(): List<PatientEntity>

    @Query("SELECT * FROM patients WHERE id = :patientId LIMIT 1")
    suspend fun getPatientById(patientId: Long): PatientEntity?

    @Insert
    suspend fun insertAssessment(assessment: AssessmentEntity): Long

    @Query("SELECT * FROM assessments WHERE patientId = :patientId ORDER BY id ASC")
    suspend fun getAssessmentsByPatientId(patientId: Long): List<AssessmentEntity>

    @Query("SELECT * FROM assessments ORDER BY id ASC")
    suspend fun getAllAssessments(): List<AssessmentEntity>
}
