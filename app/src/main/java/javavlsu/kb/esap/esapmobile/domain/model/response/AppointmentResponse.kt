package javavlsu.kb.esap.esapmobile.domain.model.response

import java.time.LocalDate
import java.time.LocalTime

data class AppointmentResponse(
    val id: Long,
    val date: LocalDate,
    val startAppointments: LocalTime,
    val endAppointments: LocalTime
)