package javavlsu.kb.esap.esapmobile.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import esapmobile.composeapp.generated.resources.*
import javavlsu.kb.esap.esapmobile.core.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    navigateToSignIn: () -> Unit
) {
    val loading by authViewModel.loading.collectAsState()
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val passwordResetResponse by authViewModel.passwordResetState.collectAsState()

    LaunchedEffect(passwordResetResponse) {
        when (passwordResetResponse) {
            is ApiResponse.Success -> {
                responseMessage = "Пароль успешно изменен"
                showDialog = true
            }
            is ApiResponse.Failure -> {
                responseMessage = (passwordResetResponse as ApiResponse.Failure).errorMessage
                showDialog = true
                authViewModel.clearPasswordResetState()
            }
            else -> {}
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (loading) {
            CircularProgress()
        } else {
            val login by authViewModel.login.collectAsState()
            val password by authViewModel.password.collectAsState()
            var passwordVisible by rememberSaveable { mutableStateOf(false) }

            ResetPasswordForm(
                login = login,
                password = password,
                passwordVisible = passwordVisible,
                onLoginChange = { authViewModel.setLogin(it) },
                onPasswordChange = { authViewModel.setPassword(it) },
                onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
                onResetPasswordButtonClick = { authViewModel.resetPassword() },
                navigateToSignIn = navigateToSignIn
            )
        }
    }

    if (showDialog) {
        ResponseDialog(responseMessage) {
            showDialog = false
            if (passwordResetResponse is ApiResponse.Success) {
                navigateToSignIn()
            }
        }
    }
}

@Composable
fun ResetPasswordForm(
    login: String,
    password: String,
    passwordVisible: Boolean,
    navigateToSignIn: () -> Unit,
    onLoginChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onResetPasswordButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(40.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.app_name),
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
                Text(stringResource(Res.string.login))
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
                Text(stringResource(Res.string.new_password))
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

        Spacer(modifier = Modifier.size(30.dp))
        CustomButton(
            text = stringResource(Res.string.reset_password),
            onClick = onResetPasswordButtonClick
        )

        TextButton(
            onClick = { navigateToSignIn() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = Color.Black)
                    ) {
                        append(text = stringResource(Res.string.have_account))
                    }
                    append(" ")
                    withStyle(
                        style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)
                    ) {
                        append(text = stringResource(Res.string.already_signin))
                    }
                },
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )
        }
    }
}
