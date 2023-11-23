package javavlsu.kb.esap.esapmobile.core.navigation.graph

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
import javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments.AppointmentBookingScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.HomeScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments.AppointmentsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments.ConfirmationScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.results.ResultsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.SettingsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.patients.PatientsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.results.AnalysisScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.results.ReportsScreen

@Composable
fun MainScreenNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.Home.route,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable(route = Screen.Main.Home.route) {
            HomeScreen(
                navigateToAppointmentsBooking = { navController.navigate(Screen.Main.AppointmentBooking.route) },
                navigateToAppointments = { navController.navigate(Screen.Main.Appointments.route) },
                navigateToMedicalCard = { navController.navigate(Screen.Main.Results.route) }
            )
        }
        composable(route = Screen.Main.AppointmentBooking.route) {
            AppointmentBookingScreen(navController)
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
            ConfirmationScreen(
                selectedDate = backStackEntry.arguments?.getString("selectedDate")!!,
                startTime = backStackEntry.arguments?.getString("startTime")!!,
                doctorId = backStackEntry.arguments?.getLong("doctorId")!!,
                scheduleId = backStackEntry.arguments?.getLong("scheduleId")!!,
                navigateBack = {
                    navController.navigate(Screen.Main.AppointmentBooking.route)
                }
            )
        }
        composable(route = Screen.Main.Appointments.route) {
            AppointmentsScreen(
                navigateToMedicalCard = { navController.navigate(Screen.Main.Results.route) }
            )
        }

        composable(route = Screen.Main.Results.route) {
            ResultsScreen(
                navController = navController
            )
        }
        composable(
            route = Screen.Main.Results.Analysis.route,
            arguments = listOf(
                navArgument("patientId") { type = NavType.LongType },
            )
        ) { backStackEntry ->
            AnalysisScreen(
                patientId = backStackEntry.arguments?.getLong("patientId")!!,
            )
        }
        composable(
            route = Screen.Main.Results.Reports.route,
            arguments = listOf(
                navArgument("patientId") { type = NavType.LongType },
            )
        ) { backStackEntry ->
            ReportsScreen(
                patientId = backStackEntry.arguments?.getLong("patientId")!!,
            )
        }
        composable(route = Screen.Main.Patients.route) {
            PatientsScreen()
        }
        composable(route = Screen.Main.More.Settings.route) {
            SettingsScreen()
        }
    }
}