package com.bclas.nutry.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.bclas.nutry.core.di.AppContainer
import com.bclas.nutry.domain.model.AnthropometricData
import com.bclas.nutry.domain.model.AttendanceHistoryItem
import com.bclas.nutry.domain.model.NutritionalAnamnesisData
import com.bclas.nutry.domain.model.Patient
import com.bclas.nutry.domain.model.PatientDraft

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
    val editingPatientId: Long? = null,
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

    data class StartEditingPatient(val patientId: Long) : PatientRegistrationAction
    data class DeletePatient(val patientId: Long) : PatientRegistrationAction
    data class DeleteAssessment(val patientId: Long, val assessmentId: Long) : PatientRegistrationAction
    data object ReloadPatients : PatientRegistrationAction
    data object ClearForm : PatientRegistrationAction
    data object DismissSaveFeedback : PatientRegistrationAction
}

class PatientRegistrationViewModel : ViewModel() {
    private val getPatientsUseCase = AppContainer.getPatientsUseCase
    private val savePatientUseCase = AppContainer.savePatientUseCase
    private val updatePatientUseCase = AppContainer.updatePatientUseCase
    private val deletePatientUseCase = AppContainer.deletePatientUseCase
    private val saveAssessmentForPatientUseCase = AppContainer.saveAssessmentForPatientUseCase
    private val deleteAssessmentUseCase = AppContainer.deleteAssessmentUseCase

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
                val isEditing = uiState.editingPatientId != null
                val draft = PatientDraft(
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
                val result = if (isEditing) {
                    updatePatientUseCase(uiState.editingPatientId ?: 0L, draft)
                } else {
                    savePatientUseCase(draft)
                }

                if (result.isSuccess) {
                    refreshPatients()
                    uiState = uiState.copy(
                        editingPatientId = null,
                        fullName = "",
                        sex = "",
                        birthDate = "",
                        phone = "",
                        email = "",
                        observations = "",
                        patientPhoto = "",
                        attendanceHistory = emptyList(),
                        saveFeedbackMessage = if (isEditing) {
                            "Paciente atualizado com sucesso."
                        } else {
                            "Cadastro realizado com sucesso."
                        },
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

            is PatientRegistrationAction.StartEditingPatient -> {
                val patient = uiState.registeredPatients.firstOrNull { it.id == action.patientId } ?: return
                uiState = uiState.copy(
                    editingPatientId = patient.id,
                    fullName = patient.fullName,
                    sex = patient.sex,
                    birthDate = patient.birthDate,
                    phone = patient.phone,
                    email = patient.email,
                    observations = patient.observations,
                    patientPhoto = patient.patientPhoto,
                    attendanceHistory = patient.attendanceHistory,
                    saveFeedbackMessage = null
                )
                historyItemIdCounter = (patient.attendanceHistory.maxOfOrNull { it.id } ?: 0L) + 1L
            }

            is PatientRegistrationAction.DeletePatient -> {
                val result = deletePatientUseCase(action.patientId)
                if (result.isSuccess) {
                    val isEditingDeletedPatient = uiState.editingPatientId == action.patientId
                    refreshPatients()
                    uiState = if (isEditingDeletedPatient) {
                        uiState.copy(
                            editingPatientId = null,
                            fullName = "",
                            sex = "",
                            birthDate = "",
                            phone = "",
                            email = "",
                            observations = "",
                            patientPhoto = "",
                            attendanceHistory = emptyList(),
                            saveFeedbackMessage = "Paciente removido com sucesso.",
                            saveFeedbackSuccess = true
                        )
                    } else {
                        uiState.copy(
                            saveFeedbackMessage = "Paciente removido com sucesso.",
                            saveFeedbackSuccess = true
                        )
                    }
                } else {
                    uiState = uiState.copy(
                        saveFeedbackMessage = result.exceptionOrNull()?.message
                            ?: "Nao foi possivel remover o paciente.",
                        saveFeedbackSuccess = false
                    )
                }
            }

            is PatientRegistrationAction.DeleteAssessment -> {
                val result = deleteAssessmentUseCase(action.patientId, action.assessmentId)
                if (result.isSuccess) {
                    refreshPatients()
                    uiState = uiState.copy(
                        saveFeedbackMessage = "Avaliacao removida do historico.",
                        saveFeedbackSuccess = true
                    )
                } else {
                    uiState = uiState.copy(
                        saveFeedbackMessage = result.exceptionOrNull()?.message
                            ?: "Nao foi possivel remover a avaliacao.",
                        saveFeedbackSuccess = false
                    )
                }
            }

            PatientRegistrationAction.ReloadPatients -> {
                refreshPatients()
            }

            PatientRegistrationAction.ClearForm -> {
                uiState = uiState.copy(
                    editingPatientId = null,
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
