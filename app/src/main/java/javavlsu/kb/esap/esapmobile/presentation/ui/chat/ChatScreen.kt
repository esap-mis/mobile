package javavlsu.kb.esap.esapmobile.presentation.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.core.data.ChatViewModel
import javavlsu.kb.esap.esapmobile.core.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.chat.ChatMessage
import javavlsu.kb.esap.esapmobile.core.domain.model.chat.ChatRoles
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.component.chat.MessengerItemCard
import javavlsu.kb.esap.esapmobile.presentation.component.chat.ReceiverMessageItemCard
import javavlsu.kb.esap.esapmobile.presentation.component.chat.ToolbarMessage
import javavlsu.kb.esap.esapmobile.presentation.component.chat.WriteMessageCard

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = hiltViewModel()
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val (messages, setMessages) = remember { mutableStateOf(listOf<ChatMessage>()) }
    val (input, setInput) = remember { mutableStateOf("") }

    fun sendMessage(message: String) {
        if (message.isNotEmpty()) {
            chatViewModel.sendMessage(message,
                object : CoroutinesErrorHandler {
                    override fun onError(message: String) {
                        responseMessage = message
                        showDialog = true
                    }
                }
            )
            setMessages(messages + ChatMessage(ChatRoles.YOU, message))
            setInput("")
        }
    }

    val messageResponse by chatViewModel.messageResponse.observeAsState()
    LaunchedEffect(messageResponse) {
        if (messageResponse is ApiResponse.Success) {
            val message = (messageResponse as ApiResponse.Success).data.message
            val chatMessage = ChatMessage(ChatRoles.BOT, message.trim('"'))
            setMessages(messages + chatMessage)
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            ToolbarMessage()
        },
        floatingActionButton = {
            WriteMessageCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                value = input,
                onValueChange = { value ->
                    setInput(value)
                },
                onClickSend = {
                    sendMessage(input)
                },
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                items(messages) { message ->
                    if (message.role == ChatRoles.YOU) {
                        MessengerItemCard(
                            modifier = Modifier.align(Alignment.End),
                            message = message.content
                        )
                    } else {
                        ReceiverMessageItemCard(message = message.content)
                    }
                }
            }
        }
    }

    if (showDialog) {
        ResponseDialog(responseMessage) {
            showDialog = false
        }
    }
}