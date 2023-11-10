package javavlsu.kb.esap.esapmobile.domain.model.request

data class AppointmentRequest(
    val patientId: Long,
    val date: String,
    val startAppointments: String
)