package javavlsu.kb.esap.esapmobile.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleField(title: String, color: Color = Color.Blue) {
    Text(
        text = title,
        fontSize = 44.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(30.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(value: String, text: String, isPassword: Boolean = false, onValueChange: (String) -> Unit) {
    if (isPassword) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text) },
            visualTransformation = PasswordVisualTransformation()
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text) }
        )
    }
}

@Composable
fun Button(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue),
        modifier = Modifier
            .padding(15.dp)
    ) {
        Text(text)
    }
}

@Composable
fun ResponseDialog(responseMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Server Response") },
        text = { Text(responseMessage) },
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue),
            ) {
                Text("OK")
            }
        }
    )
}