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
    val upcomingAppointmentsState by mainViewModel.upcomingAppointmentsState.collectAsState()
    val pastAppointmentsState by mainViewModel.pastAppointmentsState.collectAsState()
    val cancelState by mainViewModel.cancelAppointmentState.collectAsState()
    var isUpcoming by remember { mutableStateOf(true) }
    var showCancelDialog by remember { mutableStateOf(false) }
    var appointmentIdToCancel by remember { mutableStateOf<Long?>(null) }
    var showResponseDialog by remember { mutableStateOf(false) }
    var responseMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        mainViewModel.getUpcomingAppointments()
        mainViewModel.getPastAppointments()
    }

    LaunchedEffect(cancelState) {
        if (cancelState is ApiResponse.Failure) {
            responseMessage = (cancelState as ApiResponse.Failure).errorMessage
            showResponseDialog = true
        }
    }

    val showLoading = loading || upcomingAppointmentsState is ApiResponse.Loading || pastAppointmentsState is ApiResponse.Loading || cancelState is ApiResponse.Loading

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
                val patientState = mainViewModel.patientState.collectAsState().value
                val doctorState = mainViewModel.doctorState.collectAsState().value

                if (patientState is ApiResponse.Success) {
                    Header(
                        user = patientState.data,
                        isHome = false,
                        onMedicalCardClick = { navigateToMedicalCard() }
                    )
                } else if (doctorState is ApiResponse.Success) {
                    Header(
                        user = doctorState.data,
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

                val appointments = if (isUpcoming) {
                    (upcomingAppointmentsState as? ApiResponse.Success)?.data
                } else {
                    (pastAppointmentsState as? ApiResponse.Success)?.data
                }

                DisplayAppointments(
                    appointments = appointments?.sortedBy { it.getDateTime() },
                    isUpcoming = isUpcoming,
                    onCancelClick = { id ->
                        appointmentIdToCancel = id
                        showCancelDialog = true
                    }
                )
            }
        }
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Отмена записи") },
            text = { Text("Вы уверены, что хотите отменить эту запись?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        appointmentIdToCancel?.let { mainViewModel.cancelAppointment(it) }
                        showCancelDialog = false
                    }
                ) {
                    Text("Да, отменить", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCancelDialog = false }) {
                    Text("Назад")
                }
            }
        )
    }

    if (showResponseDialog) {
        ResponseDialog(responseMessage) {
            showResponseDialog = false
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
            .background(MaterialTheme.colorScheme.surfaceVariant)
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
                    containerColor = if (isUpcoming) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isUpcoming) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = stringResource(Res.string.future_appointments),
                    color = if (isUpcoming) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
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
                    containerColor = if (!isUpcoming) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (!isUpcoming) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = stringResource(Res.string.past_appointments),
                    color = if (!isUpcoming) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun DisplayAppointments(
    appointments: List<AppointmentResponse>?,
    isUpcoming: Boolean,
    onCancelClick: (Long) -> Unit
) {
    if (!appointments.isNullOrEmpty()) {
        LazyColumn {
            items(appointments) { appointment ->
                AppointmentCard(
                    appointment = appointment,
                    isUpcoming = isUpcoming,
                    onCancelClick = onCancelClick
                )
            }
        }
    } else {
        Text(stringResource(Res.string.dont_have_appointments))
    }
}

@Composable
fun AppointmentCard(
    appointment: AppointmentResponse,
    isUpcoming: Boolean,
    onCancelClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(35.dp)
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Text(
                        text = stringResource(Res.string.record),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    val parsedTime = LocalTime.parse(appointment.startAppointments, DateTimeFormatter.ofPattern("HH:mm:ss"))
                    val parsedAppointmentDate = LocalDate.parse(appointment.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    Text(
                        text = "${parsedAppointmentDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} в ${parsedTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = appointment.doctor.specialization,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    } else if (appointment.patient != null) {
                        Text(
                            text = "${appointment.patient.lastName} ${appointment.patient.firstName} ${appointment.patient.patronymic}",
                            fontWeight = FontWeight.W500,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = appointment.patient.birthDate,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                        )
                        if (appointment.doctor != null) {
                            Text(
                                text = appointment.doctor.clinic!!.address,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        } else if (appointment.patient != null) {
                            Text(
                                text = appointment.patient.clinic!!.address,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    }
                    
                    if (isUpcoming) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onCancelClick(appointment.id) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Отменить запись")
                        }
                    }
                }
            }
        }
    }
}