package com.bclas.nutry.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [PatientEntity::class, AssessmentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NutryDatabase : RoomDatabase() {
    abstract fun patientDao(): PatientDao

    companion object {
        @Volatile
        private var INSTANCE: NutryDatabase? = null

        fun getInstance(context: Context): NutryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NutryDatabase::class.java,
                    "nutry.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
