package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.apply_button
import esapmobile.composeapp.generated.resources.current_url
import esapmobile.composeapp.generated.resources.new_url
import esapmobile.composeapp.generated.resources.settings
import esapmobile.composeapp.generated.resources.warning
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

            Text(
                text = stringResource(Res.string.current_url) + currentBaseUrl,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = newBaseUrl,
                onValueChange = { newBaseUrl = it },
                label = { Text(stringResource(Res.string.new_url)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )
            Text(
                text = stringResource(Res.string.warning),
                fontSize = 12.sp,
                color = Color.Gray,
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
        }
    }

    if (showDialog) {
        ResponseDialog(responseMessage) {
            showDialog = false
        }
    }
}
