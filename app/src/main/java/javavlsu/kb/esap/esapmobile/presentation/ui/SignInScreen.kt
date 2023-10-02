package javavlsu.kb.esap.esapmobile.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.data.TokenViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.presentation.component.Button

@Composable
fun SignInScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
    navigateToSignUp: () -> Unit,
    navigateToMain: () -> Unit,
    navigateBack: () -> Unit
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val token by tokenViewModel.token.observeAsState()
    val serverStatusResponse by authViewModel.serverStatusResponse.observeAsState()
    val authResponse by authViewModel.authResponse.observeAsState()

    LaunchedEffect(true) {
        authViewModel.checkServerStatus(
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                    showDialog = true
                }
            }
        )
    }

    fun handleServerStatusSuccess() {
        if (token != null) {
            navigateToMain()
        } else {
            when (authResponse) {
                is ApiResponse.Failure -> {
                    responseMessage = (authResponse as ApiResponse.Failure).errorMessage
                    showDialog = true
                }
                is ApiResponse.Success -> {
                    val token = (authResponse as ApiResponse.Success).data.jwt
                    tokenViewModel.saveToken(token)
                }
                else -> {}
            }
        }
    }

    LaunchedEffect(serverStatusResponse, authResponse, token) {
        when (serverStatusResponse) {
            is ApiResponse.Success ->
                handleServerStatusSuccess()
            is ApiResponse.Failure -> {
                responseMessage = (serverStatusResponse as ApiResponse.Failure).errorMessage
                showDialog = true
            }
            else -> {}
        }
    }

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

        if (serverStatusResponse is ApiResponse.Loading) {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            if (serverStatusResponse is ApiResponse.Success) {
                if (authResponse is ApiResponse.Loading) {
                    CircularProgressIndicator(
                        color = Color.Blue,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    val login = authViewModel.login.value
                    val password = authViewModel.password.value
                    var passwordVisible by rememberSaveable { mutableStateOf(false) }

                    AuthForm(
                        login = login,
                        password = password,
                        passwordVisible = passwordVisible,
                        onLoginChange = { authViewModel.setLogin(it) },
                        onPasswordChange = { authViewModel.setPassword(it) },
                        onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                        onForgotPasswordButtonClick = { navigateToSignUp() },
                        onSignInButtonClick = {
                            authViewModel.login(
                                object : CoroutinesErrorHandler {
                                    override fun onError(message: String) {
                                        responseMessage = message
                                        showDialog = true
                                    }
                                }
                            )
                        },
                        onRegisterButtonClick = { navigateToSignUp() }
                    )
                }
            }
        }

        if (showDialog) {
            ResponseDialog(responseMessage) {
                showDialog = false
                navigateBack()
            }
        }
    }
}

@Composable
fun AuthForm(
    login: String,
    password: String,
    passwordVisible: Boolean,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onForgotPasswordButtonClick: () -> Unit,
    onSignInButtonClick: () -> Unit,
    onRegisterButtonClick: () -> Unit
) {
    OutlinedTextField(
        value = login,
        onValueChange = onLoginChange,
        shape = MaterialTheme.shapes.medium,
        label = { Text("Логин") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.size(30.dp))

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        shape = MaterialTheme.shapes.medium,
        label = { Text("Пароль") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Скрыть" else "Показать"
            IconButton(onClick = {
                onPasswordVisibilityToggle()
            }) {
                Icon(image, description)
            }
        }
    )

    ForgotPasswordButton(onForgotPasswordButtonClick)

    Button(
        text = "Войти",
        onClick = onSignInButtonClick
    )

    RegisterButton(onRegisterButtonClick)
}

@Composable
fun ForgotPasswordButton(onClick: () -> Unit) {
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
        TextButton(onClick = onClick) {
            Text(
                text = "Забыли пароль?",
                color = Color.Black
            )
        }
    }
}

@Composable
fun RegisterButton(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(color = Color.Black)
                ) {
                    append("Еще нет аккаунта?")
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