package javavlsu.kb.esap.esapmobile.core.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentResponse(
    val id: Long,
    val date: String? = null,
    val timeSlot: TimeSlotResponse,
    val doctor: DoctorResponse? = null,
    val patient: PatientResponse? = null,
    val status: AppointmentStatus
)