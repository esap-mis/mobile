package javavlsu.kb.esap.esapmobile.presentation.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import javavlsu.kb.esap.esapmobile.core.data.ChatViewModel
import javavlsu.kb.esap.esapmobile.core.domain.model.chat.ChatRoles
import javavlsu.kb.esap.esapmobile.core.domain.model.request.ChatRequest
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.component.chat.MessengerItemCard
import javavlsu.kb.esap.esapmobile.presentation.component.chat.ReceiverMessageItemCard
import javavlsu.kb.esap.esapmobile.presentation.component.chat.ToolbarMessage
import javavlsu.kb.esap.esapmobile.presentation.component.chat.WriteMessageCard
import org.koin.compose.koinInject
import java.util.UUID

@Composable
fun ChatScreen(
    chatViewModel: ChatViewModel = koinInject(),
    mainPadding: PaddingValues = PaddingValues()
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val (input, setInput) = remember { mutableStateOf("") }
    val sessionId by remember { mutableStateOf(UUID.randomUUID().toString()) }
    val lazyListState = rememberLazyListState()

    fun sendMessage(message: String) {
        if (message.isNotEmpty()) {
            chatViewModel.sendMessage(ChatRequest(sessionId = sessionId, message = message))
            setInput("")
        }
    }

    LaunchedEffect(chatViewModel.messages.size) {
        lazyListState.animateScrollToItem(chatViewModel.messages.size)
    }

    Scaffold(
        modifier = Modifier.padding(mainPadding).imePadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            ToolbarMessage(onClickBack = {})
        },
        bottomBar = {
            WriteMessageCard(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                value = input,
                onValueChange = setInput,
                onClickSend = { sendMessage(input) },
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 8.dp,
                        start = 8.dp,
                        end = 8.dp
                    ),
                state = lazyListState,
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                items(chatViewModel.messages) { message ->
                    if (message.role == ChatRoles.YOU) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            MessengerItemCard(
                                message = message.content
                            )
                        }
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