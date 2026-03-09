package com.bclas.nutry.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fullName: String,
    val sex: String,
    val birthDate: String,
    val phone: String,
    val email: String,
    val observations: String,
    val patientPhoto: String,
    val attendanceHistorySerialized: String
)
