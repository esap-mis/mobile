package javavlsu.kb.esap.esapmobile.presentation.ui

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
import javavlsu.kb.esap.esapmobile.core.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = koinViewModel(),
    navigateToSignIn: () -> Unit
) {
    val loading by viewModel.loading.collectAsState()
    val authResponse by viewModel.authState.collectAsState()
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Регистратор") }
    val roles = listOf("Регистратор", "Врач")
    var selectedGender by remember { mutableStateOf("Мужской") }
    val gender = listOf("Мужской", "Женский")

    LaunchedEffect(authResponse) {
        when (authResponse) {
            is ApiResponse.Success -> {
                navigateToSignIn()
            }
            is ApiResponse.Failure -> {
                responseMessage = (authResponse as ApiResponse.Failure).errorMessage
                showDialog = true
                viewModel.clearAuthState()
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
                    value = firstName,
                    onValueChange = { firstName = it },
                    shape = MaterialTheme.shapes.medium,
                    label = { Text("Имя") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(30.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    shape = MaterialTheme.shapes.medium,
                    label = { Text("Фамилия") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.size(30.dp))
                ExposedDropdownMenu(
                    options = gender,
                    selectedOption = selectedGender,
                    onOptionSelected = { selectedGender = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Пол"
                )

                Spacer(modifier = Modifier.size(30.dp))
                ExposedDropdownMenu(
                    options = roles,
                    selectedOption = selectedRole,
                    onOptionSelected = { selectedRole = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = "Роль"
                )

                Spacer(modifier = Modifier.size(30.dp))
                OutlinedTextField(
                    value = specialization,
                    onValueChange = { specialization = it },
                    shape = MaterialTheme.shapes.medium,
                    label = { Text("Специализация") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.size(30.dp))
                CustomButton(
                    text = "Зарегистрироваться",
                    onClick = { /* TODO: viewModel.register(...) */ }
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
                                append("Уже есть аккаунт?")
                            }
                            append(" ")
                            withStyle(
                                style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)
                            ) {
                                append("Войдите")
                            }
                        },
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showDialog) {
        ResponseDialog(responseMessage) {
            showDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenu(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            modifier = modifier.menuAnchor(),
            readOnly = true,
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            shape = MaterialTheme.shapes.medium,
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
