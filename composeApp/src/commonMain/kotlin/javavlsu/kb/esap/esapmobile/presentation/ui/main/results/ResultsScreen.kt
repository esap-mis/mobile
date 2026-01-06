package javavlsu.kb.esap.esapmobile.presentation.ui.main.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import esapmobile.composeapp.generated.resources.*
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.api.ApiResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.UserResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.MedicalCardResponse
import javavlsu.kb.esap.esapmobile.core.navigation.Screen
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.Header
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ResultsScreen(
    navController: NavController,
    mainViewModel: MainViewModel = koinViewModel(),
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val patientResponse by mainViewModel.patientState.collectAsState()
    val medicalCardResponse by mainViewModel.medicalCardState.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.getPatient()
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
                    mainViewModel.getPatientMedicalCard(user.id!!,)
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
                title = stringResource(Res.string.analysis),
                content = "${stringResource(Res.string.analysis)} : $analysisCount",
                icon = Res.drawable.analyses,
                navigateToScreen = {
                    if (analysisCount > 0) {
                        navController.navigate("results/analysis/${user.id}")
                    }
                }
            )
        }

        item {
            ResultCard(
                title = stringResource(Res.string.records),
                content = "${stringResource(Res.string.records_count)} ${medicalCard.medicalRecord.size}",
                icon = Res.drawable.medical_reports,
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
    icon: DrawableResource,
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
                        painter = painterResource(icon),
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