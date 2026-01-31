package javavlsu.kb.esap.esapmobile.presentation.ui.main.doctors

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
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.doctors_list
import esapmobile.composeapp.generated.resources.no_matching_patients
import esapmobile.composeapp.generated.resources.search_doctor
import javavlsu.kb.esap.esapmobile.core.data.MainViewModel
import javavlsu.kb.esap.esapmobile.core.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.presentation.component.CircularProgress
import javavlsu.kb.esap.esapmobile.presentation.component.ResponseDialog
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DoctorsScreen(
    mainViewModel: MainViewModel = koinViewModel(),
) {
    var responseMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val doctorsList = mainViewModel.doctorsList.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        mainViewModel.getDoctorsList()
    }

    if (doctorsList.itemCount < 0) {
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
                    text = stringResource(Res.string.doctors_list),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            DoctorsList(doctors = doctorsList)
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
fun DoctorsList(
    doctors: LazyPagingItems<DoctorResponse>
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
        placeholder = { Text(text = stringResource(Res.string.search_doctor)) },
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
    if (doctors.itemCount > 0) {
        LazyColumn {
            items(doctors.itemCount) { index ->
                val patient = doctors[index]
                if (patient!!.firstName.contains(searchText, ignoreCase = true) ||
                    patient.lastName.contains(searchText, ignoreCase = true) ||
                    patient.patronymic.contains(searchText, ignoreCase = true)
                ) {
                    DisplayDoctorCard(patient)
                }
            }
        }
    } else {
        Text(text = stringResource(Res.string.no_matching_patients))
    }
}

@Composable
fun DisplayDoctorCard(
    doctor: DoctorResponse
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = doctor.specialization,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = doctor.clinic!!.address,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}