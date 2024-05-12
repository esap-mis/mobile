package javavlsu.kb.esap.esapmobile.core.domain.dto.response

data class ScheduleResponse(
    val id: Long,
    val date: String,
    val startDoctorAppointment: String,
    val endDoctorAppointment: String,
    val maxPatientPerDay: Int,
    val appointments: List<AppointmentResponse>
)