package javavlsu.kb.esap.esapmobile.core.navigation.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import javavlsu.kb.esap.esapmobile.core.navigation.Screen
import javavlsu.kb.esap.esapmobile.presentation.ui.chat.ChatScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.HomeScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.SettingsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments.AppointmentBookingScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments.AppointmentsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments.ConfirmationScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.doctors.DoctorsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.patients.PatientsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.results.AnalysisScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.results.ReportsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.results.ResultsScreen

@Composable
fun MainScreenNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.Home.route
    ) {
        composable(route = Screen.Main.Home.route) {
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                HomeScreen(
                    navigateToAppointmentsBooking = { navController.navigate(Screen.Main.AppointmentBooking.route) },
                    navigateToAppointments = { navController.navigate(Screen.Main.Appointments.route) },
                    navigateToMedicalCard = { navController.navigate(Screen.Main.Results.route) }
                )
            }
        }
        composable(route = Screen.Main.AppointmentBooking.route) {
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                AppointmentBookingScreen(navController)
            }
        }
        composable(
            route = Screen.Main.AppointmentBooking.Confirmation.route,
            arguments = listOf(
                navArgument("selectedDate") { type = NavType.StringType },
                navArgument("startTime") { type = NavType.StringType },
                navArgument("doctorId") { type = NavType.LongType },
                navArgument("scheduleId") { type = NavType.LongType },
            )
        ) { backStackEntry ->
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                ConfirmationScreen(
                    selectedDate = backStackEntry.savedStateHandle.get<String>("selectedDate")!!,
                    startTime = backStackEntry.savedStateHandle.get<String>("startTime")!!,
                    doctorId = backStackEntry.savedStateHandle.get<Long>("doctorId")!!,
                    scheduleId = backStackEntry.savedStateHandle.get<Long>("scheduleId")!!,
                    navigateBack = {
                        navController.navigate(Screen.Main.AppointmentBooking.route)
                    }
                )
            }
        }
        composable(route = Screen.Main.Appointments.route) {
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                AppointmentsScreen(
                    navigateToMedicalCard = { navController.navigate(Screen.Main.Results.route) }
                )
            }
        }

        composable(route = Screen.Main.Results.route) {
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                ResultsScreen(
                    navController = navController
                )
            }
        }
        composable(
            route = Screen.Main.Results.Analysis.route,
            arguments = listOf(
                navArgument("patientId") { type = NavType.LongType },
            )
        ) { backStackEntry ->
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                AnalysisScreen(
                    patientId = backStackEntry.savedStateHandle.get<Long>("patientId")!!,
                    onBackPressed = {
                        navController.navigate(Screen.Main.Results.route)
                    }
                )
            }
        }
        composable(
            route = Screen.Main.Results.Reports.route,
            arguments = listOf(
                navArgument("patientId") { type = NavType.LongType },
            )
        ) { backStackEntry ->
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                ReportsScreen(
                    patientId = backStackEntry.savedStateHandle.get<Long>("patientId")!!,
                    onBackPressed = {
                        navController.navigate(Screen.Main.Results.route)
                    }
                )
            }
        }
        composable(route = Screen.Main.Doctors.route) {
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                DoctorsScreen()
            }
        }
        composable(route = Screen.Main.Patients.route) {
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                PatientsScreen()
            }
        }
        composable(route = Screen.Main.More.Settings.route) {
            Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
                SettingsScreen()
            }
        }
        composable(route = Screen.Main.More.Chat.route) {
            ChatScreen(mainPadding = paddingValues)
        }
    }
}