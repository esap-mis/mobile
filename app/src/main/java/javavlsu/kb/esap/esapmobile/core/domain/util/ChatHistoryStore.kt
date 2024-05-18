package javavlsu.kb.esap.esapmobile.core.domain.util

import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import javavlsu.kb.esap.esapmobile.core.domain.model.chat.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatHistoryStore(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("chat_history", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val messagesKey = "messages"

    private val _messagesFlow = MutableStateFlow(loadMessages())
    val messagesFlow: StateFlow<List<ChatMessage>> = _messagesFlow

    private fun saveMessages(messages: List<ChatMessage>) {
        val messagesJson = gson.toJson(messages)
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
            gson.fromJson(messagesJson, object : TypeToken<List<ChatMessage>>() {}.type)
        } else {
            emptyList()
        }
    }
}