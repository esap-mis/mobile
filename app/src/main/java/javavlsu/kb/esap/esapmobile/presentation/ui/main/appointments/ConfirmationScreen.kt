package javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.core.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.request.AppointmentRequest
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.PatientResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import javavlsu.kb.esap.esapmobile.presentation.theme.Green80
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ConfirmationScreen(
    selectedDate: String,
    startTime: String,
    scheduleId: Long,
    doctorId: Long,
    mainViewModel: MainViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val doctorResponse by mainViewModel.doctorResponseById.observeAsState()
    val patientResponse by mainViewModel.patientResponse.observeAsState()
    val makeAppointmentResponse by mainViewModel.makeAppointmentResponse.observeAsState()

    LaunchedEffect(doctorId) {
        mainViewModel.getDoctorById(doctorId,
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                    showDialog = true
                }
            }
        )
        mainViewModel.getPatient(
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                    showDialog = true
                }
            }
        )
    }

    if (doctorResponse is ApiResponse.Loading || patientResponse is ApiResponse.Loading || makeAppointmentResponse is ApiResponse.Loading) {
        CircularProgress()
    } else {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (doctorResponse is ApiResponse.Success) {
                    val selectedDoctor = (doctorResponse as ApiResponse.Success).data
                    DoctorDetails(
                        appointmentDate = selectedDate,
                        appointmentTime = startTime,
                        doctor = selectedDoctor
                    )
                }

                if (patientResponse is ApiResponse.Success) {
                    val patient = (patientResponse as ApiResponse.Success).data
                    PatientDetails(patient = patient)

                    Spacer(modifier = Modifier.size(16.dp))
                    CustomButton(
                        text = stringResource(R.string.confirm_appointment),
                        color = Green80,
                        onClick = {
                            mainViewModel.makeAppointment(scheduleId,
                                AppointmentRequest(patient.id, selectedDate, startTime),
                                object : CoroutinesErrorHandler {
                                    override fun onError(message: String) {
                                        responseMessage = message
                                        showDialog = true
                                    }
                                }
                            )
                            showDialog = true
                        }
                    )
                }
            }
        }
        if (showDialog && makeAppointmentResponse is ApiResponse.Success) {
            ResponseDialog(
                responseMessage = stringResource(R.string.success_making_appointment)
            ) {
                showDialog = false
                navigateBack()
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
fun DoctorDetails(
    appointmentDate: String,
    appointmentTime: String,
    doctor: DoctorResponse
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Gray40)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                tint = Color.Gray,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            DetailRow(
                text = "${doctor.lastName} ${doctor.firstName} ${doctor.patronymic}",
                textWeight = FontWeight.W500,
                textSize = 18.sp,
                isSpacing = false,
                label = doctor.specialization
            )
        }
    }
    Divider(modifier = Modifier.padding(vertical = 16.dp))
    val parsedAppointmentDate = LocalDate.parse(appointmentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    DetailRow(
        text = "${parsedAppointmentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} в $appointmentTime",
        label = stringResource(R.string.appointment_date_time)
    )
    DetailRow(
        text = doctor.clinic.address,
        label = stringResource(R.string.clinic_address)
    )
    DetailRow(
        text = doctor.clinic.phoneNumber,
        label = stringResource(R.string.clinic_phone)
    )
}

@Composable
fun PatientDetails(
    patient: PatientResponse,
) {
    DetailRow(
        text = "${patient.lastName} ${patient.firstName} ${patient.patronymic}",
        label = stringResource(R.string.patient)
    )
    DetailRow(
        text = patient.phoneNumber,
        label = stringResource(R.string.patient_phone)
    )
}

@Composable
fun DetailRow(
    text: String,
    label: String,
    isSpacing: Boolean = true,
    textSize: TextUnit = 19.sp,
    textWeight: FontWeight = FontWeight.Normal,
) {
    Text(
        text = text,
        fontSize = textSize,
        fontWeight = textWeight
    )
    if (isSpacing) {
        Spacer(modifier = Modifier.height(4.dp))
    }
    Text(
        text = label,
        color = Color.Gray,
        fontSize = 16.sp
    )
    if (isSpacing) {
        Spacer(modifier = Modifier.height(20.dp))
    }
}