package javavlsu.kb.esap.esapmobile.presentation.ui.main.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.core.data.CoroutinesErrorHandler
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.UserResponse
import javavlsu.kb.esap.esapmobile.core.domain.dto.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.Header
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.core.navigation.Screen
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40

@Composable
fun ResultsScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val patientResponse by mainViewModel.patientResponse.observeAsState()
    val medicalCardResponse by mainViewModel.medicalCardResponse.observeAsState()

    LaunchedEffect(Unit) {
        mainViewModel.getPatient(
            object : CoroutinesErrorHandler {
                override fun onError(message: String) {
                    responseMessage = message
                    showDialog = true
                }
            }
        )
    }

    if (patientResponse is ApiResponse.Loading) {
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
                    onMedicalCardClick = { navController.navigate(Screen.Main.Results.route) }
                )

                LaunchedEffect(patientResponse) {
                    mainViewModel.getPatientMedicalCard(user.id,
                        object : CoroutinesErrorHandler {
                            override fun onError(message: String) {
                                responseMessage = message
                                showDialog = true
                            }
                        }
                    )
                }

                if (medicalCardResponse is ApiResponse.Success) {
                    val medicalCard = (medicalCardResponse as ApiResponse.Success).data
                    MedicalCardRecords(
                        user = user,
                        medicalCard = medicalCard,
                        navController = navController
                    )
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
fun MedicalCardRecords(
    user: UserResponse,
    medicalCard: MedicalCardResponse,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            val analysisCount = medicalCard.medicalRecord
                .flatMap { it.analyzes }
                .count()

            ResultCard(
                title = stringResource(R.string.analysis),
                content = "${stringResource(R.string.analysis)} : $analysisCount",
                icon = R.drawable.analyses,
                navigateToScreen = {
                    if (analysisCount > 0) {
                        navController.navigate("results/analysis/${user.id}")
                    }
                }
            )
        }

        item {
            ResultCard(
                title = stringResource(R.string.records),
                content = "${stringResource(R.string.records_count)} ${medicalCard.medicalRecord.size}",
                icon = R.drawable.medical_reports,
                navigateToScreen = {
                    if (medicalCard.medicalRecord.isNotEmpty()) {
                        navController.navigate("results/report/${user.id}")
                    }
                }
            )
        }
    }
}

@Composable
fun ResultCard(
    title: String,
    content: String,
    icon: Int,
    navigateToScreen: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(Modifier.padding(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(40.dp))
                        .background(Gray40)
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )
                Text(
                    text = content,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            IconButton(
                onClick = { navigateToScreen() },
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    tint = Color.Gray,
                    contentDescription = null,
                    modifier = Modifier
                        .size(15.dp)
                )
            }
        }
    }
}