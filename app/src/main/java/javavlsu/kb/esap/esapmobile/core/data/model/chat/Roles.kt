package javavlsu.kb.esap.esapmobile.core.data.model.chat

import androidx.room.TypeConverter

enum class ChatRoles {
    YOU,
    BOT
}

class ChatRolesConverter {
    @TypeConverter
    fun fromString(value: String): ChatRoles {
        return enumValueOf(value)
    }

    @TypeConverter
    fun enumToString(role: ChatRoles): String {
        return role.name
    }
}