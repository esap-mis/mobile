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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import javavlsu.kb.esap.esapmobile.presentation.theme.Green20
import javavlsu.kb.esap.esapmobile.presentation.theme.Green80
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentsScreen(
    mainViewModel: MainViewModel = hiltViewModel(),
    tokenViewModel: TokenViewModel = hiltViewModel(),
    navigateToMedicalCard: () -> Unit
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val roles by tokenViewModel.roles.observeAsState()
    val doctorResponse by mainViewModel.doctorResponse.observeAsState()
    val patientResponse by mainViewModel.patientResponse.observeAsState()
    val userAppointmentList by mainViewModel.userAppointmentList.observeAsState()
    var isUpcoming by remember { mutableStateOf(true) }

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
                Header(
                    user = user,
                    isHome = false,
                    onMedicalCardClick = { navigateToMedicalCard() }
                )
            } else if (doctorResponse is ApiResponse.Success) {
                val user = (doctorResponse as ApiResponse.Success).data
                Header(
                    user = user,
                    isHome = false,
                    onMedicalCardClick = { navigateToMedicalCard() }
                )
            }

            CustomToggleSwitch(onToggle = {
                isUpcoming = !isUpcoming
            })
            Divider(
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

    if (showDialog) {
        ResponseDialog(responseMessage) {
            showDialog = false
        }
    }
}

@Composable
fun CustomToggleSwitch(
    onToggle: () -> Unit
) {
    var isUpcoming by remember { mutableStateOf(true) }

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
                    isUpcoming = true
                    onToggle()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isUpcoming) Color.White else Gray40,
                    contentColor = if (isUpcoming) Color.Black else Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = stringResource(R.string.future_appointments),
                    color = if (isUpcoming) Color.Black else Color.Gray
                )
            }
            Button(
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    isUpcoming = false
                    onToggle()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isUpcoming) Color.White else Gray40,
                    contentColor = if (!isUpcoming) Color.Black else Color.White
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    fontSize = 16.sp,
                    text = stringResource(R.string.past_appointments),
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
        Text(stringResource(R.string.dont_have_appointments))
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
            Row(Modifier.padding(8.dp)) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(35.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Green20)
                ) {
                    Text(
                        text = stringResource(R.string.record),
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
            Divider(Modifier.padding(8.dp))
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
                    Divider()
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
                                fontSize = 14.sp
                            )
                        } else if (appointment.patient != null) {
                            Text(
                                text = appointment.patient.clinic.address,
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