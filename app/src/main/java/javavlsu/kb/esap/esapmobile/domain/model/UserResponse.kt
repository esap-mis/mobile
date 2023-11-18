package javavlsu.kb.esap.esapmobile.domain.model

import javavlsu.kb.esap.esapmobile.domain.model.response.ClinicResponse

interface UserResponse {
    val id: Long
    val firstName: String
    val patronymic: String
    val lastName: String
    val gender: Int
    val clinic: ClinicResponse
}
