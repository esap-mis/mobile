package javavlsu.kb.esap.esapmobile.core.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class TimeSlotResponse(
    val id: Long,
    val startTime: String,
    val endTime: String,
    val isAvailable: Boolean
)