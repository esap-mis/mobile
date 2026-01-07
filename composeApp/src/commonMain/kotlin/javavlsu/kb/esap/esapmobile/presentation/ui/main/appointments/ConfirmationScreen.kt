package javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.appointment_date_time
import esapmobile.composeapp.generated.resources.clinic_address
import esapmobile.composeapp.generated.resources.clinic_phone
import esapmobile.composeapp.generated.resources.confirm_appointment
import esapmobile.composeapp.generated.resources.patient
import esapmobile.composeapp.generated.resources.patient_phone
import esapmobile.composeapp.generated.resources.success_making_appointment
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.request.AppointmentRequest
import javavlsu.kb.esap.esapmobile.core.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import javavlsu.kb.esap.esapmobile.presentation.theme.Green80
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ConfirmationScreen(
    selectedDate: String,
    startTime: String,
    scheduleId: Long,
    doctorId: Long,
    mainViewModel: MainViewModel = koinViewModel(),
    navigateBack: () -> Unit
) {
    val loading by mainViewModel.loading.collectAsState()
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val doctorResponse by mainViewModel.doctorByIdState.collectAsState()
    val patientResponse by mainViewModel.patientState.collectAsState()
    val makeAppointmentResponse by mainViewModel.makeAppointmentState.collectAsState()

    LaunchedEffect(doctorId) {
        mainViewModel.getDoctorById(doctorId)
        mainViewModel.getPatient()
    }

    if (loading) {
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
                        text = stringResource(Res.string.confirm_appointment),
                        color = Green80,
                        onClick = {
                            mainViewModel.makeAppointment(scheduleId,
                                AppointmentRequest(patient.id!!, selectedDate, startTime)
                            )
                            showDialog = true
                        }
                    )
                }
            }
        }
        if (showDialog && makeAppointmentResponse is ApiResponse.Success) {
            ResponseDialog(
                responseMessage = stringResource(Res.string.success_making_appointment)
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
    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
    val parsedAppointmentDate = LocalDate.parse(appointmentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    DetailRow(
        text = "${parsedAppointmentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} в $appointmentTime",
        label = stringResource(Res.string.appointment_date_time)
    )
    DetailRow(
        text = doctor.clinic!!.address,
        label = stringResource(Res.string.clinic_address)
    )
    DetailRow(
        text = doctor.clinic.phoneNumber,
        label = stringResource(Res.string.clinic_phone)
    )
}

@Composable
fun PatientDetails(
    patient: PatientResponse,
) {
    DetailRow(
        text = "${patient.lastName} ${patient.firstName} ${patient.patronymic}",
        label = stringResource(Res.string.patient)
    )
    DetailRow(
        text = patient.phoneNumber,
        label = stringResource(Res.string.patient_phone)
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