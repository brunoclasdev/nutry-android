package com.bclas.nutry.presentation.viewmodel

import com.bclas.nutry.core.di.AppContainer
import com.bclas.nutry.domain.model.AnthropometricData
import com.bclas.nutry.domain.model.AttendanceHistoryItem
import com.bclas.nutry.domain.model.NutritionalAnamnesisData
import com.bclas.nutry.domain.model.Patient
import com.bclas.nutry.domain.model.PatientDraft
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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
    val anamnesis: NutritionalAnamnesisData,
    val anthropometric: AnthropometricData
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
        val anamnesis: NutritionalAnamnesisData,
        val anthropometric: AnthropometricData
    ) : PatientRegistrationAction
    data object ReloadPatients : PatientRegistrationAction
    data object ClearForm : PatientRegistrationAction
    data object DismissSaveFeedback : PatientRegistrationAction
}

class PatientRegistrationViewModel : ViewModel() {
    private val getPatientsUseCase = AppContainer.getPatientsUseCase
    private val savePatientUseCase = AppContainer.savePatientUseCase
    private val saveAssessmentForPatientUseCase = AppContainer.saveAssessmentForPatientUseCase

    var uiState by mutableStateOf(PatientRegistrationUiState())
        private set

    private var historyItemIdCounter = 1L

    init {
        refreshPatients()
    }

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
                val result = savePatientUseCase(
                    PatientDraft(
                        fullName = uiState.fullName,
                        sex = uiState.sex,
                        birthDate = uiState.birthDate,
                        phone = uiState.phone,
                        email = uiState.email,
                        observations = uiState.observations,
                        patientPhoto = uiState.patientPhoto,
                        attendanceHistory = uiState.attendanceHistory.map {
                            AttendanceHistoryItem(it.id, it.description)
                        }
                    )
                )
                if (result.isSuccess) {
                    refreshPatients()
                    uiState = uiState.copy(
                        fullName = "",
                        sex = "",
                        birthDate = "",
                        phone = "",
                        email = "",
                        observations = "",
                        patientPhoto = "",
                        attendanceHistory = emptyList(),
                        saveFeedbackMessage = "Cadastro realizado com sucesso.",
                        saveFeedbackSuccess = true
                    )
                } else {
                    val message = result.exceptionOrNull()?.message ?: "Nao foi possivel salvar."
                    uiState = uiState.copy(
                        saveFeedbackMessage = message,
                        saveFeedbackSuccess = false
                    )
                }
            }

            is PatientRegistrationAction.SaveAssessmentForPatient -> {
                val result = saveAssessmentForPatientUseCase(
                    patientId = action.patientId,
                    anamnesis = action.anamnesis,
                    anthropometric = action.anthropometric
                )
                if (result.isFailure) {
                    uiState = uiState.copy(
                        saveFeedbackMessage = result.exceptionOrNull()?.message ?: "Falha ao salvar avaliacao.",
                        saveFeedbackSuccess = false
                    )
                } else {
                    uiState = uiState.copy(
                        saveFeedbackMessage = "Avaliacao salva no historico com sucesso.",
                        saveFeedbackSuccess = true
                    )
                }
                refreshPatients()
            }

            PatientRegistrationAction.ReloadPatients -> {
                refreshPatients()
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

    private fun refreshPatients() {
        uiState = uiState.copy(
            registeredPatients = getPatientsUseCase().map { it.toUiState() }
        )
    }

    fun saveAssessmentForPatient(
        patientId: Long,
        anamnesis: NutritionalAnamnesisData,
        anthropometric: AnthropometricData
    ): Boolean {
        val result = saveAssessmentForPatientUseCase(
            patientId = patientId,
            anamnesis = anamnesis,
            anthropometric = anthropometric
        )

        if (result.isFailure) {
            uiState = uiState.copy(
                saveFeedbackMessage = result.exceptionOrNull()?.message ?: "Falha ao salvar avaliacao.",
                saveFeedbackSuccess = false
            )
            return false
        }

        refreshPatients()
        uiState = uiState.copy(
            saveFeedbackMessage = "Avaliacao salva no historico com sucesso.",
            saveFeedbackSuccess = true
        )
        return true
    }
}

private fun Patient.toUiState(): RegisteredPatientUiState {
    return RegisteredPatientUiState(
        id = id,
        fullName = fullName,
        sex = sex,
        birthDate = birthDate,
        phone = phone,
        email = email,
        observations = observations,
        patientPhoto = patientPhoto,
        attendanceHistory = attendanceHistory.map {
            AttendanceHistoryItemUiState(it.id, it.description)
        },
        assessmentHistory = assessmentHistory.map {
            PatientAssessmentRecordUiState(
                id = it.id,
                createdAt = it.createdAt,
                anamnesis = it.anamnesis,
                anthropometric = it.anthropometric
            )
        }
    )
}
