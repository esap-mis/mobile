package javavlsu.kb.esap.esapmobile.domain.model.response

data class AuthResponse(
    val jwt: String,
    val roles: String
)