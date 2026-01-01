package javavlsu.kb.esap.esapmobile.core.domain.util

import android.content.Context
import javavlsu.kb.esap.esapmobile.core.domain.model.chat.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ChatHistoryStore(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("chat_history", Context.MODE_PRIVATE)
    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val messagesKey = "messages"

    private val _messagesFlow = MutableStateFlow(loadMessages())
    val messagesFlow: StateFlow<List<ChatMessage>> = _messagesFlow

    private fun saveMessages(messages: List<ChatMessage>) {
        val messagesJson = json.encodeToString(messages)
        sharedPreferences.edit().putString(messagesKey, messagesJson).apply()
        _messagesFlow.value = messages
    }

    fun saveMessage(message: ChatMessage) {
        val messages = _messagesFlow.value.toMutableList()
        messages.add(message)
        saveMessages(messages)
    }

    private fun loadMessages(): List<ChatMessage> {
        val messagesJson = sharedPreferences.getString(messagesKey, null)
        return if (!messagesJson.isNullOrEmpty()) {
            try {
                json.decodeFromString<List<ChatMessage>>(messagesJson)
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }
}