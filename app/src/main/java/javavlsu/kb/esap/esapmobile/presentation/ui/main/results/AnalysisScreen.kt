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
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.AnalysisResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AnalysisScreen(
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
                        text = stringResource(R.string.analysis),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                AnalysisList(medicalCard = medicalCard)
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
fun AnalysisList(
    medicalCard: MedicalCardResponse
) {
    val searchText = remember { mutableStateOf("") }
    val filteredMedicalRecords = medicalCard.medicalRecord.filter { medicalRecord ->
        medicalRecord.analyzes.any { analysis ->
            analysis.name.contains(searchText.value, ignoreCase = true) ||
                    analysis.date.contains(searchText.value, ignoreCase = true)
        }
    }

    SearchBar(
        query = searchText.value,
        onQueryChange = { text ->
            searchText.value = text
        },
        onSearch = {},
        active = false,
        onActiveChange = {},
        placeholder = { Text(text = stringResource(R.string.search_analysis)) },
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
        Column {
            filteredMedicalRecords.forEach { medicalRecord ->
                DisplayAnalysis(medicalRecord.analyzes)
            }
        }
    } else {
        Text(text = stringResource(R.string.no_analysis))
    }
}

@Composable
fun DisplayAnalysis(analysis: List<AnalysisResponse>) {
    if (analysis.isNotEmpty()) {
        LazyColumn {
            items(analysis) { analysisItem ->
                AnalysisCard(analysisItem)
            }
        }
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
            val parsedDate = LocalDateTime.parse(analysis.date, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            Text(
                text = "${parsedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))} в ${parsedDate.format(
                    DateTimeFormatter.ofPattern("HH:mm"))}",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}