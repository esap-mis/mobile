package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.data.MainViewModel
import javavlsu.kb.esap.esapmobile.data.TokenViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.AnalysisResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.component.Header
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import javavlsu.kb.esap.esapmobile.presentation.theme.Green80
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
    navigateToAppointmentsBooking: () -> Unit,
    navigateToAppointments: () -> Unit,
    navigateToAnalisis: () -> Unit
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val roles by tokenViewModel.roles.observeAsState()
    val doctorResponse by mainViewModel.doctorResponse.observeAsState()
    val patientResponse by mainViewModel.patientResponse.observeAsState()
    val userAppointmentList by mainViewModel.userAppointmentList.observeAsState()
    val medicalCardResponse by mainViewModel.medicalCardResponse.observeAsState()

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
        mainViewModel.getUserAppointments(
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
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (patientResponse is ApiResponse.Success) {
                val user = (patientResponse as ApiResponse.Success).data
                Header(user = user)
                Spacer(modifier = Modifier.size(16.dp))
                CustomButton(
                    text = stringResource(R.string.make_appointment),
                    color = Green80,
                    onClick = navigateToAppointmentsBooking
                )

                LaunchedEffect(patientResponse) {
                    mainViewModel.getPatientMedicalCard(user.id,
                        object : CoroutinesErrorHandler {
                            override fun onError(message: String) {
                                responseMessage = message
                                showDialog = true
                            }
                        }
                    )
                }

                if (medicalCardResponse is ApiResponse.Success) {
                    val medicalCard = (medicalCardResponse as ApiResponse.Success).data

                    val analysis = medicalCard.medicalRecord
                        .flatMap { record -> record.analyzes }
                        .sortedBy { it.date }
                        .take(5)

                    Spacer(modifier = Modifier.height(16.dp))
                    DisplayAnalysis(
                        analysis = analysis,
                        onAllClick = { navigateToAnalisis() }
                    )
                }

            } else if (doctorResponse is ApiResponse.Success) {
                val user = (doctorResponse as ApiResponse.Success).data
                Header(user = user)
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

    if (showDialog) {
        ResponseDialog(responseMessage) {
            showDialog = false
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
                    text = stringResource(R.string.next_aapointments),
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
                            text = stringResource(R.string.all),
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
        Text(stringResource(R.string.dont_have_next_appointments))
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
                    Divider()
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
                                text = appointment.doctor.clinic.address,
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        } else if (appointment.patient != null) {
                            Text(
                                text = appointment.patient.clinic.address,
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
                    text = stringResource(R.string.analysis_results),
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
                            text = stringResource(R.string.all),
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
        Text(text = stringResource(R.string.no_results))
    }
}

@Composable
fun AnalysisCard(analysis: AnalysisResponse) {
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
            Text(
                text = analysis.date,
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = analysis.name,
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