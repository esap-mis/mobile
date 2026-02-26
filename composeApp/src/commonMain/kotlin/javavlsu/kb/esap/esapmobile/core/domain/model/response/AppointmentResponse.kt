package javavlsu.kb.esap.esapmobile.core.domain.model.response

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializable
data class AppointmentResponse(
    val id: Long,
    val date: String,
    val startAppointments: String,
    val endAppointments: String,
    val doctor: DoctorResponse? = null,
    val patient: PatientResponse? = null,
    val status: AppointmentStatus
) {
    fun getDateTime(): LocalDateTime {
        return LocalDateTime.parse("$date $startAppointments", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    }
}