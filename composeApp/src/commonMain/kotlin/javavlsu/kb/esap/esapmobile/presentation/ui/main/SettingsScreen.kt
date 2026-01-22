package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import esapmobile.composeapp.generated.resources.*
import javavlsu.kb.esap.esapmobile.core.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.core.data.SettingsViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var newBaseUrl by remember { mutableStateOf("") }
    val currentBaseUrl by settingsViewModel.baseUrl.collectAsState()
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
    val serverStatusResponse by authViewModel.serverStatusState.collectAsState()

    var applyButtonClicked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                text = stringResource(Res.string.settings),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Theme Settings
            Text(
                text = stringResource(Res.string.theme_setting),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ThemeOptionButton(
                    text = stringResource(Res.string.system_theme),
                    isSelected = isDarkMode == null,
                    onClick = { settingsViewModel.setIsDarkMode(null) },
                    modifier = Modifier.weight(1f)
                )
                ThemeOptionButton(
                    text = stringResource(Res.string.light_theme),
                    isSelected = isDarkMode == false,
                    onClick = { settingsViewModel.setIsDarkMode(false) },
                    modifier = Modifier.weight(1f)
                )
                ThemeOptionButton(
                    text = stringResource(Res.string.dark_theme),
                    isSelected = isDarkMode == true,
                    onClick = { settingsViewModel.setIsDarkMode(true) },
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = stringResource(Res.string.current_url) + currentBaseUrl,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = newBaseUrl,
                onValueChange = { newBaseUrl = it },
                label = { Text(stringResource(Res.string.new_url)) },
                visualTransformation = UrlVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )
            Text(
                text = stringResource(Res.string.warning),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            CustomButton(
                text = stringResource(Res.string.apply_button),
                isEnabled = newBaseUrl.isNotBlank(),
                onClick = {
                    applyButtonClicked = true
                    settingsViewModel.setBaseUrl(newBaseUrl)
                    authViewModel.checkServerStatus()
                }
            )
        }
    }

    val changeUrlSuccessMessage = stringResource(Res.string.change_url_cuccess)
    LaunchedEffect(serverStatusResponse) {
        if (applyButtonClicked) {
            if (serverStatusResponse is ApiResponse.Success) {
                settingsViewModel.setBaseUrl(newBaseUrl)
                responseMessage = "URL успешно изменен"
            } else if (serverStatusResponse is ApiResponse.Failure) {
                responseMessage =
                    "Ошибка при изменении URL: ${(serverStatusResponse as ApiResponse.Failure).errorMessage}"
            }
            showDialog = true
            settingsViewModel.setBaseUrl("http://$newBaseUrl")
            responseMessage = changeUrlSuccessMessage
        }
    }

    if (showDialog) {
        ResponseDialog(responseMessage) {
            showDialog = false
        }
    }
}

@Composable
fun ThemeOptionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
