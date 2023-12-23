package javavlsu.kb.esap.esapmobile.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
import com.google.firebase.messaging.FirebaseMessaging
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.core.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.core.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.core.data.NotificationViewModel
import javavlsu.kb.esap.esapmobile.core.data.TokenViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog

@Composable
fun SignInScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
    notificationViewModel: NotificationViewModel = hiltViewModel(),
    navigateToSignUp: () -> Unit,
    navigateToMain: () -> Unit,
    navigateBack: () -> Unit,
    navigateToForgotPassword: () -> Unit
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
            if (authResponse is ApiResponse.Success) {
                val response = (authResponse as ApiResponse.Success).data
                tokenViewModel.saveToken(response.jwt)
                tokenViewModel.saveRoles(response.roles)

                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@addOnCompleteListener
                    }
                    val deviceToken = task.result
                    notificationViewModel.registerDeviceToken(
                        deviceToken,
                        object : CoroutinesErrorHandler {
                            override fun onError(message: String) {
                                responseMessage = message
                                showDialog = true
                            }
                        }
                    )
                }
            } else if (authResponse is ApiResponse.Failure) {
                responseMessage = (authResponse as ApiResponse.Failure).errorMessage
                showDialog = true
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

    if (serverStatusResponse is ApiResponse.Loading) {
        CircularProgress()
    } else {
        if (serverStatusResponse is ApiResponse.Success) {
            if (authResponse is ApiResponse.Loading) {
                CircularProgress()
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
                    onForgotPasswordButtonClick = { navigateToForgotPassword() },
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
    Column(
        modifier = Modifier
            .padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 44.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Blue,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(30.dp))

        OutlinedTextField(
            value = login,
            onValueChange = onLoginChange,
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(stringResource(R.string.login))
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = null)
            }
        )
        Spacer(modifier = Modifier.size(30.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(stringResource(R.string.password))
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = {
                    onPasswordVisibilityToggle()
                }) {
                    Icon(imageVector = image, contentDescription = null)
                }
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
            }
        )

        ForgotPasswordButton(onForgotPasswordButtonClick)

        CustomButton(
            text = stringResource(R.string.signin),
            onClick = onSignInButtonClick
        )

        RegisterButton(onRegisterButtonClick)
    }
}

@Composable
fun ForgotPasswordButton(onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = onClick) {
            Text(
                text = stringResource(R.string.forgot_password),
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
                    append(stringResource(R.string.dont_have_account))
                }
                append(" ")
                withStyle(
                    style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)
                ) {
                    append(stringResource(R.string.register))
                }
            },
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center
        )
    }
}