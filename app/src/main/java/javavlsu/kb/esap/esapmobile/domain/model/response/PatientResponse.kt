package javavlsu.kb.esap.esapmobile.domain.model.response

import javavlsu.kb.esap.esapmobile.domain.model.UserResponse

data class PatientResponse(
    val birthDate: String,
    val address: String,
    val phoneNumber: String,
    val email: String,
    override val id: Long,
    override val firstName: String,
    override val patronymic: String,
    override val lastName: String,
    override val gender: Int,
    override val clinic: ClinicResponse,
) : UserResponse