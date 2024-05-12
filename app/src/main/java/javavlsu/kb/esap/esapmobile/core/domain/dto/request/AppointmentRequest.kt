package javavlsu.kb.esap.esapmobile.core.domain.dto.request

data class AppointmentRequest(
    val patientId: Long,
    val date: String,
    val startAppointments: String
)