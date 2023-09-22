package javavlsu.kb.esap.esapmobile.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import javavlsu.kb.esap.esapmobile.data.AuthViewModel

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel
) {
    val login = authViewModel.login.value
    var password = authViewModel.password.value
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "ЕСАП",
            fontSize = 44.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(30.dp))
        OutlinedTextField(
            value = login,
            onValueChange = {
                authViewModel.login.value = it
            },
            shape = MaterialTheme.shapes.medium,
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(30.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                authViewModel.password.value = it
            },
            shape = MaterialTheme.shapes.medium,
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Скрыть" else "Пароль"
                IconButton(onClick = {
                    passwordVisible = !passwordVisible}){
                    Icon(image, description)
                }
            }
        )

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

            }
            TextButton(onClick = {
                //navigator.navigate(ForgotPasswordScreenDestination)
            }) {
                Text(
                    text = "Забыли пароль?",
                    color = Color.Black
                )
            }
        }

        Button(text = "Войти") {
            authViewModel.performLogin(login, password) { result ->
                responseMessage = result
                showDialog = true
            }
        }

        TextButton(
            onClick = {
//                navigator.popBackStack()
//                navigator.navigate(RegisterScreenDestination)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = Color.Black)
                    ) {
                        append("Еще нет акаунта?")
                    }
                    append(" ")
                    withStyle(
                        style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)
                    ) {
                        append("Зарегистрируйтесь")
                    }
                },
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        }

        if (showDialog) {
            ResponseDialog(responseMessage) {
                showDialog = false
            }
        }
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