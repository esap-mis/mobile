package javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.navigation.NavController
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.available_appointment_times
import esapmobile.composeapp.generated.resources.doctors
import esapmobile.composeapp.generated.resources.havent_doctors_data
import javavlsu.kb.esap.esapmobile.core.data.CalendarViewModel
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.presentation.component.Calendar
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.VerticalGrid
import javavlsu.kb.esap.esapmobile.presentation.data.TimeSlot
import javavlsu.kb.esap.esapmobile.presentation.data.calculateAvailableTimeSlots
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray20
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import javavlsu.kb.esap.esapmobile.presentation.theme.NightBlue
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentBookingScreen(
    navController: NavController,
    mainViewModel: MainViewModel = koinViewModel(),
    calendarViewModel: CalendarViewModel = koinViewModel(),
) {
    val loading by mainViewModel.loading.collectAsState()
    val doctorListResponse by mainViewModel.doctorListState.collectAsState()
    val selectedDate by calendarViewModel.selectedDate.collectAsState()

    LaunchedEffect(selectedDate) {
        mainViewModel.getDoctorList(selectedDate)
    }

    val showLoading = loading || doctorListResponse is ApiResponse.Loading

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
                Calendar()
                Spacer(modifier = Modifier.size(30.dp))

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Start),
                ) {
                    Text(
                        text = stringResource(Res.string.doctors),
                        fontSize = 20.sp,
                        color = NightBlue,
                        fontWeight = FontWeight.W600,
                    )
                }

                if (doctorListResponse is ApiResponse.Success<*>) {
                    val doctors = (doctorListResponse as ApiResponse.Success<List<DoctorResponse>>).data
                    if (doctors.isNotEmpty()) {
                        LazyColumn {
                            items(doctors) { doctor ->
                                DoctorCard(
                                    date = selectedDate,
                                    doctor = doctor,
                                    navController = navController
                                )
                            }
                        }
                    } else {
                        Text(stringResource(Res.string.havent_doctors_data))
                    }
                }
            }
        }
    }
}

@Composable
fun DoctorCard(
    date: LocalDate,
    doctor: DoctorResponse,
    navController: NavController
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
                    Text(
                        text = "${doctor.lastName} ${doctor.firstName} ${doctor.patronymic}",
                        fontWeight = FontWeight.W500,
                        fontSize = 20.sp
                    )
                    Text(
                        text = doctor.specialization,
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            var isExpanded by remember { mutableStateOf(false) }
            val availableTimeSlots = calculateAvailableTimeSlots(doctor.schedules!!, doctor.schedules[0].appointments)

            if (doctor.schedules.isNotEmpty()) {
                ExpandableTimeSlotsList(
                    isExpanded = isExpanded,
                    onExpandToggle = { isExpanded = !isExpanded },
                    availableTimeSlots = availableTimeSlots,
                    date = date,
                    scheduleId = doctor.schedules[0].id,
                    doctorId = doctor.id!!,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun ExpandableTimeSlotsList(
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    availableTimeSlots: List<TimeSlot>,
    date: LocalDate,
    scheduleId: Long,
    doctorId: Long,
    navController: NavController
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.available_appointment_times),
            fontWeight = FontWeight.W500,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        if (availableTimeSlots.size > 8) {
            IconButton(
                onClick = onExpandToggle,
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
    VerticalGrid(
        columns = 4,
        content = {
            val displayedTimeSlots = if (isExpanded) availableTimeSlots else availableTimeSlots.take(8)
            displayedTimeSlots.forEach { timeSlot ->
                TimeSlotCard(
                    timeSlot = timeSlot,
                    onClick = { navController.navigate("appointment/${date}/${scheduleId}/${timeSlot.startTime}/${doctorId}") }
                )
            }
        }
    )
}

@Composable
fun TimeSlotCard(
    timeSlot: TimeSlot,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(2.dp)
            .clickable { onClick() }
            .clip(RoundedCornerShape(10.dp)),
        colors = CardDefaults.cardColors(containerColor = Gray20)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 18.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = timeSlot.startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                color = Color.Gray,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}