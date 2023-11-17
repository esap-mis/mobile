package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.data.SettingsViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var newBaseUrl by remember { mutableStateOf("") }
    val currentBaseUrl by settingsViewModel.baseUrl.observeAsState()
    val serverStatusResponse by authViewModel.serverStatusResponse.observeAsState()

    var applyButtonClicked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        ) {
            Text(
                text = stringResource(R.string.settings),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = stringResource(R.string.current_url) + currentBaseUrl,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = newBaseUrl,
                onValueChange = { newBaseUrl = it },
                label = { Text(stringResource(R.string.new_url)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )
            Text(
                text = stringResource(R.string.warning),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    applyButtonClicked = true
                    settingsViewModel.changeBaseUrl(newBaseUrl)
                    if (currentBaseUrl != null && applyButtonClicked) {
                        authViewModel.checkServerStatus(
                            object : CoroutinesErrorHandler {
                                override fun onError(message: String) {
                                    responseMessage = message
                                    showDialog = true
                                }
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.apply_button))
            }
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
