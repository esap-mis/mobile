package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import esapmobile.composeapp.generated.resources.*
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AnalysisResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.component.Header
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import javavlsu.kb.esap.esapmobile.presentation.theme.Green80
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = koinViewModel(),
    navigateToAppointmentsBooking: () -> Unit,
    navigateToAppointments: () -> Unit,
    navigateToMedicalCard: () -> Unit
) {
    val loading by mainViewModel.loading.collectAsState()
    val doctorResponse by mainViewModel.doctorState.collectAsState()
    val patientResponse by mainViewModel.patientState.collectAsState()
    val userAppointmentList by mainViewModel.userAppointmentsState.collectAsState()
    val medicalCardResponse by mainViewModel.medicalCardState.collectAsState()

    val errorMessage by mainViewModel.errorMessage.collectAsStateWithLifecycle()
    var showErrorDialog by remember { mutableStateOf(false) }
    var currentErrorMessage by remember { mutableStateOf("") }

    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotBlank()) {
            currentErrorMessage = errorMessage
            showErrorDialog = true
        }
    }

    LaunchedEffect(patientResponse) {
        if (patientResponse is ApiResponse.Success) {
            val patient = (patientResponse as ApiResponse.Success).data
            patient.id?.let { patientId ->
                mainViewModel.getPatientMedicalCard(patientId)
            }
        }
    }

    if (loading) {
        CircularProgress()
    } else {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (patientResponse is ApiResponse.Success) {
                val user = (patientResponse as ApiResponse.Success).data
                Header(
                    user = user,
                    onMedicalCardClick = { navigateToMedicalCard() }
                )
                Spacer(modifier = Modifier.size(16.dp))
                CustomButton(
                    text = stringResource(Res.string.make_appointment),
                    color = Green80,
                    onClick = navigateToAppointmentsBooking
                )

                if (medicalCardResponse is ApiResponse.Success) {
                    val medicalCard = (medicalCardResponse as ApiResponse.Success).data

                    val analysis = medicalCard.medicalRecord
                        .flatMap { record -> record.analyzes }
                        .sortedBy { it.date }
                        .take(5)

                    Spacer(modifier = Modifier.height(16.dp))
                    DisplayAnalysis(
                        analysis = analysis,
                        onAllClick = { navigateToMedicalCard() }
                    )
                }

            } else if (doctorResponse is ApiResponse.Success) {
                val user = (doctorResponse as ApiResponse.Success).data
                Header(
                    user = user,
                    onMedicalCardClick = { navigateToMedicalCard() }
                )
            }

            if (userAppointmentList is ApiResponse.Success) {
                var appointments = (userAppointmentList as ApiResponse.Success).data

                appointments = appointments
                    .filter { it.isUpcoming() }
                    .sortedBy { it.getDateTime() }
                    .take(5)

                Spacer(modifier = Modifier.height(16.dp))
                DisplayNextAppointments(
                    appointments = appointments,
                    onAllClick = navigateToAppointments
                )
            }
        }
    }

    if (showErrorDialog) {
        ResponseDialog(currentErrorMessage) {
            showErrorDialog = false
        }
    }
}

@Composable
fun DisplayNextAppointments(
    appointments: List<AppointmentResponse>?,
    onAllClick: () -> Unit
) {
    if (!appointments.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.next_appointments),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Black,
                    textAlign = TextAlign.Left
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onAllClick() }
                        .padding(end = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.all),
                            textAlign = TextAlign.Right,
                            fontSize = 18.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(5.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            tint = Color.Gray,
                            contentDescription = null,
                            modifier = Modifier
                                .size(15.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(appointments) { appointment ->
                    NextAppointmentCard(appointment = appointment)
                }
            }
        }
    } else {
        Text(stringResource(Res.string.dont_have_next_appointments))
    }
}

@Composable
fun NextAppointmentCard(
    appointment: AppointmentResponse
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(Modifier.padding(8.dp)) {
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
                    if (appointment.doctor != null) {
                        Text(
                            text = "${appointment.doctor.lastName} ${appointment.doctor.firstName} ${appointment.doctor.patronymic}",
                            fontWeight = FontWeight.W500,
                            fontSize = 18.sp
                        )
                        Text(
                            text = appointment.doctor.specialization,
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    } else if (appointment.patient != null) {
                        Text(
                            text = "${appointment.patient.lastName} ${appointment.patient.firstName} ${appointment.patient.patronymic}",
                            fontWeight = FontWeight.W500,
                            fontSize = 18.sp
                        )
                        Text(
                            text = appointment.patient.birthDate,
                            color = Color.Gray,
                            fontSize = 16.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            tint = Color.Gray,
                            contentDescription = null,
                        )
                        val parsedTime = LocalTime.parse(appointment.startAppointments, DateTimeFormatter.ofPattern("HH:mm:ss"))
                        val parsedAppointmentDate = LocalDate.parse(appointment.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        Text(
                            text = "${parsedAppointmentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} в ${parsedTime.format(
                                DateTimeFormatter.ofPattern("HH:mm"))}",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            tint = Color.Gray,
                            contentDescription = null,
                        )
                        if (appointment.doctor != null) {
                            Text(
                                text = appointment.doctor.clinic!!.address,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        } else if (appointment.patient != null) {
                            Text(
                                text = appointment.patient.clinic!!.address,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayAnalysis(
    analysis: List<AnalysisResponse>?,
    onAllClick: () -> Unit
) {
    if (!analysis.isNullOrEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.results),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                    color = Color.Black,
                    textAlign = TextAlign.Left
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onAllClick() }
                        .padding(end = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.all),
                            textAlign = TextAlign.Right,
                            fontSize = 18.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(5.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            tint = Color.Gray,
                            contentDescription = null,
                            modifier = Modifier
                                .size(15.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(analysis) { analysis ->
                    AnalysisCard(analysis = analysis)
                }
            }
        }
    } else {
        Text(text = stringResource(Res.string.no_results))
    }
}

@Composable
fun AnalysisCard(
    analysis: AnalysisResponse
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            val parsedDate = LocalDateTime.parse(analysis.date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            Text(
                text = "${parsedDate.format(DateTimeFormatter.ofPattern("dd MMMM"))} в ${parsedDate.format(
                    DateTimeFormatter.ofPattern("HH:mm"))}",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = analysis.name.take(15),
                fontSize = 18.sp,
                fontWeight = FontWeight.W500
            )
            Text(
                text = analysis.result,
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}