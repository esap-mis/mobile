package javavlsu.kb.esap.esapmobile.presentation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import javavlsu.kb.esap.esapmobile.domain.model.response.ClinicResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.DoctorResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.presentation.data.TimeSlot
import javavlsu.kb.esap.esapmobile.presentation.navigation.Screen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments.AppointmentBookingScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.HomeScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments.AppointmentsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments.ConfirmationScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.ResultsScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.SettingsScreen

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
                onMakeAppointmentClick = {
                    navController.navigate(Screen.Main.AppointmentBooking.route)
                }
            )
        }
        composable(route = Screen.Main.AppointmentBooking.route) {
            AppointmentBookingScreen(navController)
        }

        composable(
            route = Screen.Main.AppointmentBooking.Confirmation.route,
            arguments = listOf(
                navArgument("selectedDate") { type = NavType.StringType },
                navArgument("timeSlot") { type = NavType.StringType },
                navArgument("doctorId") { type = NavType.LongType },
            )
        ) { backStackEntry ->
            ConfirmationScreen(
                selectedDate = backStackEntry.arguments?.getString("selectedDate")!!,
                timeSlot = backStackEntry.arguments?.getString("timeSlot")!!,
                doctorId = backStackEntry.arguments?.getLong("doctorId")!!
            )
        }
        
        composable(route = Screen.Main.Appointments.route) {
            AppointmentsScreen()
        }
        composable(route = Screen.Main.Results.route) {
            ResultsScreen()
        }
        composable(route = Screen.Main.More.Settings.route) {
            SettingsScreen()
        }
    }
}