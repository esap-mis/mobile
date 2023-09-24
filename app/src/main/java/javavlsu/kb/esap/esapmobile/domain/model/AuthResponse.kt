package javavlsu.kb.esap.esapmobile.domain.model

data class AuthResponse(
    val jwt: String,
    val roles: String
)