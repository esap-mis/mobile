package javavlsu.kb.esap.esapmobile.core.domain.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val sessionId: String,
    val message: String
)