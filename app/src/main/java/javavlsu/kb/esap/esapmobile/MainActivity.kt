package javavlsu.kb.esap.esapmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import javavlsu.kb.esap.esapmobile.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.ui.component.*
import javavlsu.kb.esap.esapmobile.ui.theme.EsapMobileTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EsapMobileTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
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
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TitleField("ЕСАП")
        TextField(
            text = "Логин",
            value = login,
        ) { login = it }
        TextField(
            text = "Пароль",
            value = password,
            isPassword = true,
        ) { password = it }
        Button(text = "Войти") {
            authViewModel.performLogin(login, password) { result ->
                responseMessage = result
                showDialog = true
            }
        }

        if (showDialog) {
            ResponseDialog(responseMessage) {
                showDialog = false
            }
        }
    }
}