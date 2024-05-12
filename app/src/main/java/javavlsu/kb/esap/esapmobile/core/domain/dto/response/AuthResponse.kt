package javavlsu.kb.esap.esapmobile.core.domain.dto.response

data class AuthResponse(
    val jwt: String,
    val roles: String
)