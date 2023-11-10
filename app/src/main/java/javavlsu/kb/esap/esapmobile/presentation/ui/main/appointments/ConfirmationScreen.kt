package javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.data.MainViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.presentation.component.Button
import javavlsu.kb.esap.esapmobile.presentation.theme.Green80
import java.time.LocalDate

@Composable
fun ConfirmationScreen(
    selectedDate: String,
    timeSlot: String,
    doctorId: Long,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    var responseMessage by remember { mutableStateOf("") }
    val doctorListResponse by mainViewModel.doctorListResponse.observeAsState()

    LaunchedEffect(doctorId) {
        mainViewModel.getDoctorById(
            doctorId,
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                }
            }
        )
    }

    LaunchedEffect(selectedDate) {
        mainViewModel.getDoctorList(
            LocalDate.parse(selectedDate),
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                }
            }
        )
    }

    if (doctorListResponse is ApiResponse.Success) {
        val selectedDoctor = (doctorListResponse as ApiResponse.Success).data.find { it.id == doctorId }

        Text("Doctor: ${selectedDoctor?.firstName} ${selectedDoctor?.lastName}")

        Spacer(modifier = Modifier.size(32.dp))
        Button(
            text = stringResource(R.string.confirm_appointment),
            color = Green80,
            onClick = { }
        )
    }
}
