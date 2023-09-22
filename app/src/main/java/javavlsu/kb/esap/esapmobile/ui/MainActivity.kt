package javavlsu.kb.esap.esapmobile.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dagger.hilt.android.AndroidEntryPoint
import javavlsu.kb.esap.esapmobile.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.ui.component.*
import javavlsu.kb.esap.esapmobile.ui.theme.EsapMobileTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EsapMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen(authViewModel)
                }
            }
        }
    }
}

@Composable
fun AuthScreen(authViewModel: AuthViewModel) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
            fontWeight = FontWeight.Bold,
            color = Color.Blue,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(30.dp))
        TextField(
            text = "Логин",
            value = login,
        ) { login = it }
        Spacer(modifier = Modifier.size(30.dp))
        TextField(
            text = "Пароль",
            value = password,
            isPassword = true,
        ) { password = it }

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