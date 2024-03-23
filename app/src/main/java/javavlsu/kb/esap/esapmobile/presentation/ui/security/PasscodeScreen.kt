package javavlsu.kb.esap.esapmobile.presentation.ui.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun PasscodeScreen() {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter text") },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
            }),
            modifier = Modifier.padding(16.dp)
        )
        CustomKeyboard(onKeyPressed = {
            if (it == "Back") {
                if (inputText.isNotEmpty()) {
                    inputText = inputText.dropLast(1)
                }
            } else {
                inputText += it
            }
        })
    }
}

@Composable
fun CustomKeyboard(
    onKeyPressed: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            KeyboardButton(text = "1", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "2", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "3", onKeyPressed = onKeyPressed)
        }
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            KeyboardButton(text = "4", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "5", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "6", onKeyPressed = onKeyPressed)
        }
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            KeyboardButton(text = "7", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "8", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "9", onKeyPressed = onKeyPressed)
        }
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            KeyboardButton(text = "0", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "Back", onKeyPressed = onKeyPressed)
        }
    }
}

@Composable
fun KeyboardButton(
    text: String,
    onKeyPressed: (String) -> Unit
) {
    Button(
        onClick = { onKeyPressed(text) },
        modifier = Modifier
            .padding(horizontal = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        )
    ) {
        Text(text = text)
    }
}