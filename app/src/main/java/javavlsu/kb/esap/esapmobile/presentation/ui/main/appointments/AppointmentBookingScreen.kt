package javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavController
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.core.data.CalendarViewModel
import javavlsu.kb.esap.esapmobile.core.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.presentation.component.Calendar
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.VerticalGrid
import javavlsu.kb.esap.esapmobile.presentation.data.TimeSlot
import javavlsu.kb.esap.esapmobile.presentation.data.calculateAvailableTimeSlots
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray20
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import javavlsu.kb.esap.esapmobile.presentation.theme.NightBlue
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentBookingScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    calendarViewModel: CalendarViewModel = hiltViewModel(),
) {
    var responseMessage by remember { mutableStateOf("") }
    val doctorListResponse by mainViewModel.doctorListResponse.observeAsState()
    val data by calendarViewModel.calendarData.observeAsState()

    LaunchedEffect(data!!.selectedDate) {
        mainViewModel.getDoctorList(
            data!!.selectedDate.date,
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                }
            }
        )
    }

    if (doctorListResponse is ApiResponse.Loading) {
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

            Row(modifier = Modifier
                .padding(8.dp)
                .align(Alignment.Start),
            ) {
                Text(
                    text = stringResource(R.string.doctors),
                    fontSize = 20.sp,
                    color = NightBlue,
                    fontWeight = FontWeight.W600,
                )
            }
            if (doctorListResponse is ApiResponse.Success) {
                val doctors = (doctorListResponse as ApiResponse.Success).data
                if (doctors.isNotEmpty()) {
                    LazyColumn {
                        items(doctors) { doctor ->
                            DoctorCard(
                                date = data!!.selectedDate.date,
                                doctor = doctor,
                                navController = navController
                            )
                        }
                    }
                } else {
                    Text(stringResource(R.string.havent_doctors_data))
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
            val availableTimeSlots = calculateAvailableTimeSlots(doctor.schedules, doctor.schedules[0].appointments)

            if (doctor.schedules.isNotEmpty()) {
                ExpandableTimeSlotsList(
                    isExpanded = isExpanded,
                    onExpandToggle = { isExpanded = !isExpanded },
                    availableTimeSlots = availableTimeSlots,
                    date = date,
                    scheduleId = doctor.schedules[0].id,
                    doctorId = doctor.id,
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
            text = stringResource(R.string.available_appointment_times),
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