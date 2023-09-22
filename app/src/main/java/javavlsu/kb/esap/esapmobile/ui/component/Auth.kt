package javavlsu.kb.esap.esapmobile.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(value: String, text: String, isPassword: Boolean = false, onValueChange: (String) -> Unit) {
    if (isPassword) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = MaterialTheme.shapes.medium,
            label = { Text(text) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            shape = MaterialTheme.shapes.medium,
            label = { Text(text) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun Button(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color.Blue),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth()
        )
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