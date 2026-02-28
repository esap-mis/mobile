package javavlsu.kb.esap.esapmobile.core.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ScheduleResponse(
    val id: Long,
    val date: String,
    val maxPatientPerDay: Int,
    val appointments: List<AppointmentResponse>,
    val timeSlots: List<TimeSlotResponse>
)