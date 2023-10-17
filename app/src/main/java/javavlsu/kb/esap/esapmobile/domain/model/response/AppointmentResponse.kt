package javavlsu.kb.esap.esapmobile.domain.model.response

data class AppointmentResponse(
    val id: Long,
    val date: String,
    val startAppointments: String,
    val endAppointments: String
)