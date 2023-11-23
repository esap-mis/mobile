package javavlsu.kb.esap.esapmobile.core.domain.model.response

import javavlsu.kb.esap.esapmobile.core.domain.model.UserResponse

data class DoctorResponse(
    val specialization: String,
    val schedules: List<ScheduleResponse>,
    override val id: Long,
    override val firstName: String,
    override val patronymic: String,
    override val lastName: String,
    override val gender: Int,
    override val clinic: ClinicResponse,
) : UserResponse