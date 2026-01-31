package javavlsu.kb.esap.esapmobile.core.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentRequest(
    val patientId: Long,
    val date: String,
    val startAppointments: String
)