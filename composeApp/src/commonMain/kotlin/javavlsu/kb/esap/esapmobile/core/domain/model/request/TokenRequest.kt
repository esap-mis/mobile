package javavlsu.kb.esap.esapmobile.core.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class TokenRequest(
    val token: String
)