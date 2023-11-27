package javavlsu.kb.esap.esapmobile.core.domain.model.response

data class AuthResponse(
    val jwt: String,
    val roles: String
)