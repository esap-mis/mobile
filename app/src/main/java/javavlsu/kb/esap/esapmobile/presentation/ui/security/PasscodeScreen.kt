package javavlsu.kb.esap.esapmobile.presentation.ui.security

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import javavlsu.kb.esap.esapmobile.R

@Composable
fun PasscodeScreen() {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(30.dp))
        Text("Введите пароль", modifier = Modifier.padding(bottom = 16.dp))
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until 4) {
                PasscodeCircle(isFilled = i < inputText.length)
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
        Keyboard(onKeyPressed = {
            if (it == "Back") {
                if (inputText.isNotEmpty()) {
                    inputText = inputText.dropLast(1)
                }
            } else {
                if (inputText.length < 4) {
                    inputText += it
                }
            }
        })
    }
}

@Composable
fun PasscodeCircle(isFilled: Boolean) {
    val backgroundColor = if (isFilled) Color.Blue else Color.LightGray

    Box(
        modifier = Modifier
            .size(10.dp)
            .background(color = backgroundColor, shape = CircleShape),
    )
}

@Composable
fun Keyboard(
    onKeyPressed: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            KeyboardButton(text = "1", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "2", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "3", onKeyPressed = onKeyPressed)
        }
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            KeyboardButton(text = "4", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "5", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "6", onKeyPressed = onKeyPressed)
        }
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp)
        ) {
            KeyboardButton(text = "7", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "8", onKeyPressed = onKeyPressed)
            KeyboardButton(text = "9", onKeyPressed = onKeyPressed)
        }
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp)
        ) {
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
            .size(64.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text = text, modifier = Modifier.padding(8.dp))
    }
}