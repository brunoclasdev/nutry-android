package com.bclas.nutry.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query

@Dao
interface PatientDao {
    @Insert
    suspend fun insertPatient(patient: PatientEntity): Long

    @Query("SELECT * FROM patients ORDER BY id ASC")
    suspend fun getPatients(): List<PatientEntity>

    @Query("SELECT * FROM patients WHERE id = :patientId LIMIT 1")
    suspend fun getPatientById(patientId: Long): PatientEntity?

    @Update
    suspend fun updatePatient(patient: PatientEntity): Int

    @Query("DELETE FROM patients WHERE id = :patientId")
    suspend fun deletePatientById(patientId: Long): Int

    @Insert
    suspend fun insertAssessment(assessment: AssessmentEntity): Long

    @Query("SELECT * FROM assessments WHERE patientId = :patientId ORDER BY id ASC")
    suspend fun getAssessmentsByPatientId(patientId: Long): List<AssessmentEntity>

    @Query("SELECT * FROM assessments ORDER BY id ASC")
    suspend fun getAllAssessments(): List<AssessmentEntity>

    @Query("DELETE FROM assessments WHERE patientId = :patientId AND id = :assessmentId")
    suspend fun deleteAssessmentById(patientId: Long, assessmentId: Long): Int

    @Query("UPDATE patients SET attendanceHistorySerialized = :serialized WHERE id = :patientId")
    suspend fun updateAttendanceHistory(patientId: Long, serialized: String)
}
