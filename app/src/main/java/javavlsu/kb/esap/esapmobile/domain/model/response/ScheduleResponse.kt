package javavlsu.kb.esap.esapmobile.domain.model.response

import java.time.LocalDate
import java.time.LocalTime

data class ScheduleResponse(
    val id: Long,
    val date: LocalDate,
    val startDoctorAppointment: LocalTime,
    val endDoctorAppointment: LocalTime,
    val maxPatientPerDay: Int,
    val appointments: List<AppointmentResponse>
)