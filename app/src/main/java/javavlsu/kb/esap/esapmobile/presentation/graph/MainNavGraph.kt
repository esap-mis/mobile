package javavlsu.kb.esap.esapmobile.presentation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import javavlsu.kb.esap.esapmobile.presentation.navigation.Screen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.AppointmentBookingScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.HomeScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.AppointmentsScreen
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
            AppointmentBookingScreen()
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