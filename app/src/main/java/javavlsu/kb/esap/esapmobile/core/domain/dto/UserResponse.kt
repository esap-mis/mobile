package javavlsu.kb.esap.esapmobile.core.domain.dto

import javavlsu.kb.esap.esapmobile.core.domain.dto.response.ClinicResponse

interface UserResponse {
    val id: Long
    val firstName: String
    val patronymic: String
    val lastName: String
    val gender: Int
    val clinic: ClinicResponse
}
