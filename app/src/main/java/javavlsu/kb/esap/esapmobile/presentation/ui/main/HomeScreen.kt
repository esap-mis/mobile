package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.data.MainViewModel
import javavlsu.kb.esap.esapmobile.data.TokenViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.presentation.component.Button
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.Header
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Green80

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
    onMakeAppointmentClick: () -> Unit
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val roles by tokenViewModel.roles.observeAsState()
    val doctorResponse by mainViewModel.doctorResponse.observeAsState()
    val patientResponse by mainViewModel.patientResponse.observeAsState()

    LaunchedEffect(roles) {
        if (roles?.contains("ROLE_DOCTOR") == true || roles?.contains("ROLE_CHIEF_DOCTOR") == true) {
            mainViewModel.getDoctor(
                object : CoroutinesErrorHandler {
                    override fun onError(message: String) {
                        responseMessage = message
                        showDialog = true
                    }
                }
            )
        } else if (roles?.contains("ROLE_PATIENT") == true) {
            mainViewModel.getPatient(
                object : CoroutinesErrorHandler {
                    override fun onError(message: String) {
                        responseMessage = message
                        showDialog = true
                    }
                }
            )
        }
    }

    if (patientResponse is ApiResponse.Loading || doctorResponse is ApiResponse.Loading) {
        CircularProgress()
    } else {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (patientResponse is ApiResponse.Success) {
                val user = (patientResponse as ApiResponse.Success).data
                Header(user = user)
                Spacer(modifier = Modifier.size(32.dp))
                Button(
                    text = stringResource(R.string.make_appointment),
                    color = Green80,
                    onClick = onMakeAppointmentClick
                )
            } else if (doctorResponse is ApiResponse.Success) {
                val user = (doctorResponse as ApiResponse.Success).data
                Header(user = user)
            }
        }
    }

    if (showDialog) {
        ResponseDialog(responseMessage) {
            showDialog = false
        }
    }
}
