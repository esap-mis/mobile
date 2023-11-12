package javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.data.MainViewModel
import javavlsu.kb.esap.esapmobile.data.TokenViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.Header
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog


// TODO: Вынести запрос с отображением информации о пользователе вотедльный компонент, чтобы рендерить на нескольких страницах
@Composable
fun AppointmentsScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val roles by tokenViewModel.roles.observeAsState()
    val doctorResponse by mainViewModel.doctorResponse.observeAsState()
    val patientResponse by mainViewModel.patientResponse.observeAsState()
    val patientAppointmentList by mainViewModel.patientAppointmentList.observeAsState() // TODO: Пока пациент, переделать на user. Переписать запрос на сервере.

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

    LaunchedEffect(Unit) {
        mainViewModel.getPatientAppointments(
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                    showDialog = true
                }
            }
        )
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
                Header(
                    user = user,
                    isHome = false
                ) {}
            } else if (doctorResponse is ApiResponse.Success) {
                val user = (doctorResponse as ApiResponse.Success).data
                Header(
                    user = user,
                    isHome = false
                ) {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (patientAppointmentList is ApiResponse.Success) {
                val appointments = (patientAppointmentList as ApiResponse.Success).data
                if (appointments.isNotEmpty()) {
                    LazyColumn {
                        items(appointments) { appointment ->
                            AppointmentCard(appointment = appointment)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                } else {
                    Text("Нет приемов")
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

@Composable
fun AppointmentCard(appointment: AppointmentResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle card click */ }
            .padding(16.dp),
//        elevation = 8.dp
    ) {
        Column {
//            Text(text = "Doctor: ${appointment.doctor.fullName}")
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(text = "Address: ${appointment.clinic.address}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${appointment.date}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Time: ${appointment.startAppointments}")
        }
    }
}