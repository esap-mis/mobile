package javavlsu.kb.esap.esapmobile.core.domain.model.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(
    val role: ChatRoles,
    val content: String
)