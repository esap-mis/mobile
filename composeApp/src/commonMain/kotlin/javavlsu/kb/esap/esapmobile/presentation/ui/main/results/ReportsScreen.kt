package javavlsu.kb.esap.esapmobile.presentation.ui.main.results

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Search
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
import esapmobile.composeapp.generated.resources.*
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalRecordResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.DocumentCard
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ReportsScreen(
    patientId: Long,
    mainViewModel: MainViewModel = koinViewModel(),
    onBackPressed: () -> Unit
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val medicalCardResponse by mainViewModel.medicalCardState.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.getPatientMedicalCard(patientId)
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
                        text = stringResource(Res.string.records),
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
        placeholder = { Text(text = stringResource(Res.string.search_record)) },
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
        Text(text = stringResource(Res.string.no_records))
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
                title = stringResource(Res.string.record_results),
                fileName = "test-report",
                downloadUrl = "https://clck.ru/3AdYbg"
            )
        }
    }
}