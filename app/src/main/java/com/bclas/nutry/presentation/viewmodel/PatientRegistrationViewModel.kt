package com.bclas.nutry.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class AttendanceHistoryItemUiState(
    val id: Long,
    val description: String
)

data class PatientRegistrationUiState(
    val fullName: String = "",
    val sex: String = "",
    val birthDate: String = "",
    val phone: String = "",
    val email: String = "",
    val observations: String = "",
    val patientPhoto: String = "",
    val attendanceHistory: List<AttendanceHistoryItemUiState> = emptyList()
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
}

class PatientRegistrationViewModel : ViewModel() {
    var uiState by mutableStateOf(PatientRegistrationUiState())
        private set

    private var historyItemIdCounter = 1L

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
        }
    }
}
