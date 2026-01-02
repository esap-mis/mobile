package javavlsu.kb.esap.esapmobile.core.domain.model.response

import javavlsu.kb.esap.esapmobile.core.domain.model.UserResponse
import kotlinx.serialization.Serializable

@Serializable
data class DoctorResponse(
    val specialization: String,
    val schedules: List<ScheduleResponse>? = null,
    override val id: Long? = null,
    override val firstName: String,
    override val patronymic: String,
    override val lastName: String,
    override val gender: Int,
    override val clinic: ClinicResponse? = null,
) : UserResponse