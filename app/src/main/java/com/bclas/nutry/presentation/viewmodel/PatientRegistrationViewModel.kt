package com.bclas.nutry.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class AttendanceHistoryItemUiState(
    val id: Long,
    val description: String
)

data class RegisteredPatientUiState(
    val id: Long,
    val fullName: String,
    val sex: String,
    val birthDate: String,
    val phone: String,
    val email: String,
    val observations: String,
    val patientPhoto: String,
    val attendanceHistory: List<AttendanceHistoryItemUiState>,
    val assessmentHistory: List<PatientAssessmentRecordUiState> = emptyList()
)

data class PatientAssessmentRecordUiState(
    val id: Long,
    val createdAt: String,
    val anamnesis: NutritionalAnamnesisUiState,
    val anthropometric: AnthropometricAssessmentUiState
)

data class PatientRegistrationUiState(
    val fullName: String = "",
    val sex: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val email: String = "",
    val observations: String = "",
    val patientPhoto: String = "",
    val attendanceHistory: List<AttendanceHistoryItemUiState> = emptyList(),
    val registeredPatients: List<RegisteredPatientUiState> = emptyList(),
    val saveFeedbackMessage: String? = null,
    val saveFeedbackSuccess: Boolean = false
)

sealed interface PatientRegistrationAction {
    data class FullNameChanged(val value: String) : PatientRegistrationAction
    data class SexChanged(val value: String) : PatientRegistrationAction
    data class BirthDateChanged(val value: String) : PatientRegistrationAction
    data class PhoneChanged(val value: String) : PatientRegistrationAction
    data class EmailChanged(val value: String) : PatientRegistrationAction
    data class ObservationsChanged(val value: String) : PatientRegistrationAction
    data class PatientPhotoChanged(val value: String) : PatientRegistrationAction
    data class AddAttendanceHistory(val value: String) : PatientRegistrationAction
    data class RemoveAttendanceHistory(val id: Long) : PatientRegistrationAction
    data object SavePatient : PatientRegistrationAction
    data class SaveAssessmentForPatient(
        val patientId: Long,
        val anamnesis: NutritionalAnamnesisUiState,
        val anthropometric: AnthropometricAssessmentUiState
    ) : PatientRegistrationAction
    data object ClearForm : PatientRegistrationAction
    data object DismissSaveFeedback : PatientRegistrationAction
}

class PatientRegistrationViewModel : ViewModel() {
    var uiState by mutableStateOf(PatientRegistrationUiState())
        private set

    private var historyItemIdCounter = 1L
    private var patientIdCounter = 1L
    private var assessmentIdCounter = 1L

    fun onAction(action: PatientRegistrationAction) {
        when (action) {
            is PatientRegistrationAction.FullNameChanged -> {
                uiState = uiState.copy(fullName = action.value)
            }

            is PatientRegistrationAction.SexChanged -> {
                uiState = uiState.copy(sex = action.value)
            }

            is PatientRegistrationAction.BirthDateChanged -> {
                uiState = uiState.copy(birthDate = action.value)
            }

            is PatientRegistrationAction.PhoneChanged -> {
                uiState = uiState.copy(phone = action.value)
            }

            is PatientRegistrationAction.EmailChanged -> {
                uiState = uiState.copy(email = action.value)
            }

            is PatientRegistrationAction.ObservationsChanged -> {
                uiState = uiState.copy(observations = action.value)
            }

            is PatientRegistrationAction.PatientPhotoChanged -> {
                uiState = uiState.copy(patientPhoto = action.value)
            }

            is PatientRegistrationAction.AddAttendanceHistory -> {
                val description = action.value.trim()
                if (description.isBlank()) return
                uiState = uiState.copy(
                    attendanceHistory = uiState.attendanceHistory + AttendanceHistoryItemUiState(
                        id = historyItemIdCounter++,
                        description = description
                    )
                )
            }

            is PatientRegistrationAction.RemoveAttendanceHistory -> {
                uiState = uiState.copy(
                    attendanceHistory = uiState.attendanceHistory.filterNot { it.id == action.id }
                )
            }

            PatientRegistrationAction.SavePatient -> {
                val name = uiState.fullName.trim()
                if (name.isBlank()) {
                    uiState = uiState.copy(
                        saveFeedbackMessage = "Nao foi possivel salvar. Informe o nome completo.",
                        saveFeedbackSuccess = false
                    )
                    return
                }
                val registeredPatient = RegisteredPatientUiState(
                    id = patientIdCounter++,
                    fullName = name,
                    sex = uiState.sex.trim(),
                    birthDate = uiState.birthDate.trim(),
                    phone = uiState.phone.trim(),
                    email = uiState.email.trim(),
                    observations = uiState.observations.trim(),
                    patientPhoto = uiState.patientPhoto.trim(),
                    attendanceHistory = uiState.attendanceHistory
                )
                uiState = uiState.copy(
                    fullName = "",
                    sex = "",
                    birthDate = "",
                    phone = "",
                    email = "",
                    observations = "",
                    patientPhoto = "",
                    attendanceHistory = emptyList(),
                    registeredPatients = uiState.registeredPatients + registeredPatient,
                    saveFeedbackMessage = "Cadastro realizado com sucesso.",
                    saveFeedbackSuccess = true
                )
            }

            is PatientRegistrationAction.SaveAssessmentForPatient -> {
                val record = PatientAssessmentRecordUiState(
                    id = assessmentIdCounter++,
                    createdAt = nowFormatted(),
                    anamnesis = action.anamnesis.copy(feedbackMessage = null),
                    anthropometric = action.anthropometric.copy(feedbackMessage = null)
                )
                uiState = uiState.copy(
                    registeredPatients = uiState.registeredPatients.map { patient ->
                        if (patient.id == action.patientId) {
                            patient.copy(
                                assessmentHistory = patient.assessmentHistory + record
                            )
                        } else {
                            patient
                        }
                    }
                )
            }

            PatientRegistrationAction.ClearForm -> {
                uiState = uiState.copy(
                    fullName = "",
                    sex = "",
                    birthDate = "",
                    phone = "",
                    email = "",
                    observations = "",
                    patientPhoto = "",
                    attendanceHistory = emptyList(),
                    saveFeedbackMessage = null
                )
            }

            PatientRegistrationAction.DismissSaveFeedback -> {
                uiState = uiState.copy(saveFeedbackMessage = null)
            }
        }
    }
}

private fun nowFormatted(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date())
}
