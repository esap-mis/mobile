package javavlsu.kb.esap.esapmobile.domain.model.response

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class AppointmentResponse(
    val id: Long,
    val date: String,
    val startAppointments: String,
    val endAppointments: String,
    val doctor: DoctorResponse?,
    val patient: PatientResponse?
) {
    fun isUpcoming(): Boolean {
        val appointmentDateTime =
            LocalDateTime.parse("$date $startAppointments", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return appointmentDateTime.isAfter(LocalDateTime.now())
    }

    fun getDateTime(): LocalDateTime {
        return LocalDateTime.parse("$date $startAppointments", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}