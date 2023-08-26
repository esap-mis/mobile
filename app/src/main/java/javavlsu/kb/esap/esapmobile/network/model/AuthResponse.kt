package javavlsu.kb.esap.esapmobile.network.model

data class AuthResponse(
    val jwt: String,
    val roles: String
)