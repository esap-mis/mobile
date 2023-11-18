package javavlsu.kb.esap.esapmobile.presentation.ui.main.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.data.MainViewModel
import javavlsu.kb.esap.esapmobile.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.AnalysisResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Green80

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
                if (medicalCard.medicalRecord.isNotEmpty()) {
                    Column {
                        medicalCard.medicalRecord.forEach { medicalRecord ->
                            DisplayAnalysis(medicalRecord.analyzes)
                        }
                    }
                } else {
                    Text(text = "Нет анализов")
                }
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
                text = "Анализ: ${analysis.name}",
                color = Green80,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )
            Text(
                text = "Результат: ${analysis.result}",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "Дата: ${analysis.date}",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}