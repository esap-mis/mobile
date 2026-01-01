package javavlsu.kb.esap.esapmobile.core.domain.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val jwt: String,
    val roles: String
)