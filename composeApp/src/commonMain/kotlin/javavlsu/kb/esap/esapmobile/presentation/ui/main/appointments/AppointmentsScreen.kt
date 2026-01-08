package javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.dont_have_appointments
import esapmobile.composeapp.generated.resources.future_appointments
import esapmobile.composeapp.generated.resources.past_appointments
import esapmobile.composeapp.generated.resources.record
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.data.TokenViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.AppointmentResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.Header
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import javavlsu.kb.esap.esapmobile.presentation.theme.Green20
import javavlsu.kb.esap.esapmobile.presentation.theme.Green80
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentsScreen(
    mainViewModel: MainViewModel = koinViewModel(),
    tokenViewModel: TokenViewModel = koinViewModel(),
    navigateToMedicalCard: () -> Unit
) {
    val loading by mainViewModel.loading.collectAsState()
    val userAppointmentList by mainViewModel.userAppointmentsState.collectAsState()
    var isUpcoming by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        mainViewModel.getUserAppointments()
    }

    val showLoading = loading || userAppointmentList is ApiResponse.Loading

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (showLoading) {
            CircularProgress()
        } else {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (mainViewModel.patientState.collectAsState().value is ApiResponse.Success) {
                    val user = (mainViewModel.patientState.collectAsState().value as ApiResponse.Success).data
                    Header(
                        user = user,
                        isHome = false,
                        onMedicalCardClick = { navigateToMedicalCard() }
                    )
                } else if (mainViewModel.doctorState.collectAsState().value is ApiResponse.Success) {
                    val user = (mainViewModel.doctorState.collectAsState().value as ApiResponse.Success).data
                    Header(
                        user = user,
                        isHome = false,
                        onMedicalCardClick = { navigateToMedicalCard() }
                    )
                }

                CustomToggleSwitch(
                    isUpcoming = isUpcoming,
                    onToggle = {
                        isUpcoming = !isUpcoming
                    }
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(
                            vertical = 16.dp,
                            horizontal = 8.dp
                        )
                )

                if (userAppointmentList is ApiResponse.Success) {
                    var appointments = (userAppointmentList as ApiResponse.Success).data

                    appointments = if (isUpcoming) {
                        appointments.filter { it.isUpcoming() }
                    } else {
                        appointments.filter { !it.isUpcoming() }
                    }

                    DisplayAppointments(appointments.sortedBy { it.getDateTime() })
                }
            }
        }
    }
}

@Composable
fun CustomToggleSwitch(
    isUpcoming: Boolean,
    onToggle: (isUpcoming: Boolean) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(8.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Gray40)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Button(
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (!isUpcoming) {
                        onToggle(true)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isUpcoming) Color.White else Gray40,
                    contentColor = if (isUpcoming) Color.Black else Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = stringResource(Res.string.future_appointments),
                    color = if (isUpcoming) Color.Black else Color.Gray
                )
            }
            Button(
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (isUpcoming) {
                        onToggle(false)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isUpcoming) Color.White else Gray40,
                    contentColor = if (!isUpcoming) Color.Black else Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = stringResource(Res.string.past_appointments),
                    color = if (!isUpcoming) Color.Black else Color.Gray
                )
            }
        }
    }
}

@Composable
fun DisplayAppointments(appointments: List<AppointmentResponse>?) {
    if (!appointments.isNullOrEmpty()) {
        LazyColumn {
            items(appointments) { appointment ->
                AppointmentCard(appointment = appointment)
            }
        }
    } else {
        Text(stringResource(Res.string.dont_have_appointments))
    }
}

@Composable
fun AppointmentCard(
    appointment: AppointmentResponse
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(35.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Green20)
                ) {
                    Text(
                        text = stringResource(Res.string.record),
                        color = Green80,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(35.dp)
                        .width(IntrinsicSize.Max)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Gray40)
                ) {
                    val parsedTime = LocalTime.parse(appointment.startAppointments, DateTimeFormatter.ofPattern("HH:mm:ss"))
                    val parsedAppointmentDate = LocalDate.parse(appointment.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    Text(
                        text = "${parsedAppointmentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} в ${parsedTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                }
            }
            HorizontalDivider(Modifier.padding(8.dp))
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
                            fontSize = 14.sp
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
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
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
                                fontSize = 14.sp
                            )
                        } else if (appointment.patient != null) {
                            Text(
                                text = appointment.patient.clinic!!.address,
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}