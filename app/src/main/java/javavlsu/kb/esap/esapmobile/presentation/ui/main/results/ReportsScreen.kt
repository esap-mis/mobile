package javavlsu.kb.esap.esapmobile.presentation.ui.main.results

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.core.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.MedicalRecordResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ReportsScreen(
    patientId: Long,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val medicalCardResponse by mainViewModel.medicalCardResponse.observeAsState()

    LaunchedEffect(Unit) {
        mainViewModel.getPatientMedicalCard(patientId,
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                    showDialog = true
                }
            }
        )
    }

    if (medicalCardResponse is ApiResponse.Loading) {
        CircularProgress()
    } else {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (medicalCardResponse is ApiResponse.Success) {
                val medicalCard = (medicalCardResponse as ApiResponse.Success).data

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .align(Alignment.Start)
                ) {
                    Text(
                        text = stringResource(R.string.records),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                MedicalRecordList(medicalCard = medicalCard)
            }
        }
    }

    if (showDialog) {
        ResponseDialog(responseMessage) {
            showDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalRecordList(
    medicalCard: MedicalCardResponse
) {
    val searchText = remember { mutableStateOf("") }
    val isActive = remember { mutableStateOf(false) }
    val filteredMedicalRecords = medicalCard.medicalRecord.filter {
        it.record.contains(searchText.value, ignoreCase = true) ||
                it.fioAndSpecializationDoctor.contains(searchText.value, ignoreCase = true) ||
                it.date.contains(searchText.value, ignoreCase = true)
    }

    SearchBar(
        query = searchText.value,
        onQueryChange = { text ->
            searchText.value = text
        },
        onSearch = {},
        active = isActive.value,
        onActiveChange = {},
        placeholder = { Text(text = stringResource(R.string.search_record)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = null,
                modifier = Modifier
                    .size(18.dp)
                    .clickable {}
            )
        }
    ) {}

    if (filteredMedicalRecords.isNotEmpty()) {
        LazyColumn {
            items(filteredMedicalRecords) { medicalRecord ->
                DisplayMedicalRecord(medicalRecord)
            }
        }
    } else {
        Text(text = stringResource(R.string.no_records))
    }
}

@Composable
fun DisplayMedicalRecord(
    medicalRecord: MedicalRecordResponse
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
            Text(
                text = medicalRecord.record,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Text(
                text = medicalRecord.fioAndSpecializationDoctor,
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            val parsedDate = LocalDate.parse(medicalRecord.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            Text(
                text = parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}