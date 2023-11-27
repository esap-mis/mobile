package javavlsu.kb.esap.esapmobile.core.domain.model

import javavlsu.kb.esap.esapmobile.core.domain.model.response.ClinicResponse

interface UserResponse {
    val id: Long
    val firstName: String
    val patronymic: String
    val lastName: String
    val gender: Int
    val clinic: ClinicResponse
}
