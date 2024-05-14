package javavlsu.kb.esap.esapmobile.presentation.ui.main.results

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
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.core.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalRecordResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.DocumentCard
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ReportsScreen(
    patientId: Long,
    mainViewModel: MainViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackPressed,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIos,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = stringResource(R.string.records),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.W600,
                        color = Color.Black,
                        textAlign = TextAlign.Center
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
            val parsedDate = LocalDate.parse(medicalRecord.date, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            Text(
                text = parsedDate.format(DateTimeFormatter.ofPattern("dd MMMM")),
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(8.dp)
            )
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
                    val parsedDoctor = medicalRecord.fioAndSpecializationDoctor.split(':')
                    Text(
                        text = parsedDoctor[0].trim(),
                        fontWeight = FontWeight.W500,
                        fontSize = 18.sp
                    )
                    Text(
                        text = parsedDoctor[1].trim(),
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = medicalRecord.record,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }
            //FIXME переделать
            DocumentCard(
                context = LocalContext.current,
                title = stringResource(R.string.record_results),
                fileName = "test-report",
                downloadUrl = "https://clck.ru/3AdYbg"
            )
        }
    }
}