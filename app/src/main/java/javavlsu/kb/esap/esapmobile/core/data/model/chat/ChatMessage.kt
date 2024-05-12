package javavlsu.kb.esap.esapmobile.core.data.model.chat

import androidx.room.Entity
import androidx.room.TypeConverters

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @TypeConverters(ChatRolesConverter::class)
    val role: ChatRoles,
    val content: String
)