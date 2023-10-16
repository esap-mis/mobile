package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.data.MainViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.presentation.component.Calendar
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress

@Composable
fun AppointmentScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    var responseMessage by remember { mutableStateOf("") }
    val doctorListResponse by mainViewModel.doctorListResponse.observeAsState()

    LaunchedEffect(true) {
        mainViewModel.getDoctorList(
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                }
            }
        )
    }

    if (doctorListResponse is ApiResponse.Loading) {
        CircularProgress()
    } else if (doctorListResponse is ApiResponse.Success) {
        val doctors = (doctorListResponse as ApiResponse.Success).data.content
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Calendar()
            Spacer(modifier = Modifier.size(30.dp))

            Text(
                text = "Врачи",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            if (doctors.isNotEmpty()) {
                doctors.forEach { doctor ->
                    DoctorCard(doctor)
                }
            } else {
                Text("Нет данных о врачах.")
            }
        }
    }
}

@Composable
fun DoctorCard(doctor: DoctorResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "${doctor.lastName} ${doctor.firstName} ${doctor.patronymic}",
                fontWeight = FontWeight.W600,
                fontSize = 20.sp
            )
            Text(text = "Профессия: ${doctor.specialization}", fontSize = 16.sp)
            Text(text = "Клиника: ${doctor.clinic.name}", fontSize = 16.sp)

            if (doctor.schedules.isNotEmpty()) {
                Text(text = "Доступное время приема:", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                for (schedule in doctor.schedules) {
                    Text(text = "Дата: ${schedule.date}, Время: ${schedule.endDoctorAppointment}", fontSize = 16.sp)
                }
            }
        }
    }
}