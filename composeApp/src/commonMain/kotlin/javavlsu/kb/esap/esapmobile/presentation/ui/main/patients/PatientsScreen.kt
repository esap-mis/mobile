package javavlsu.kb.esap.esapmobile.presentation.ui.main.patients

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.no_matching_patients
import esapmobile.composeapp.generated.resources.patients_list
import esapmobile.composeapp.generated.resources.search_patient
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PatientsScreen(
    mainViewModel: MainViewModel = koinViewModel(),
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val patientsList = mainViewModel.patientsList.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        mainViewModel.getPatientsList()
    }

    if (patientsList.itemCount < 0) {
        CircularProgress()
    } else {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .align(Alignment.Start)
            ) {
                Text(
                    text = stringResource(Res.string.patients_list),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            PatientsList(patients = patientsList)
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
fun PatientsList(
    patients: LazyPagingItems<PatientResponse>
) {
    var searchText by remember { mutableStateOf("") }
    SearchBar(
        query = searchText,
        onQueryChange = { text ->
            searchText = text
        },
        onSearch = {},
        active = false,
        onActiveChange = {},
        placeholder = { Text(text = stringResource(Res.string.search_patient)) },
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

    Spacer(modifier = Modifier.width(16.dp))
    if (patients.itemCount > 0) {
        LazyColumn {
            items(patients.itemCount) { index ->
                val patient = patients[index]
                if (patient!!.firstName.contains(searchText, ignoreCase = true) ||
                    patient.lastName.contains(searchText, ignoreCase = true) ||
                    patient.patronymic.contains(searchText, ignoreCase = true)
                ) {
                    DisplayPatientCard(patient)
                }
            }
        }
    } else {
        Text(text = stringResource(Res.string.no_matching_patients))
    }
}

@Composable
fun DisplayPatientCard(
    patient: PatientResponse
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
                        text = "${patient.lastName} ${patient.firstName} ${patient.patronymic}",
                        fontWeight = FontWeight.W500,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = patient.birthDate,
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = patient.address,
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}