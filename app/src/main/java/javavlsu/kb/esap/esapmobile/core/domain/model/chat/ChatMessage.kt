package javavlsu.kb.esap.esapmobile.core.domain.model.chat

data class ChatMessage(
    val role: ChatRoles,
    val content: String
)