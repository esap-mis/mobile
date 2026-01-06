package javavlsu.kb.esap.esapmobile.core.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val login: String,
    val password: String
)