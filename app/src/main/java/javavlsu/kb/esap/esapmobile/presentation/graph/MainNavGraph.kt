package javavlsu.kb.esap.esapmobile.presentation.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import javavlsu.kb.esap.esapmobile.presentation.navigation.Screen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.AppointmentScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.HomeScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.ProfileScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.main.SettingsScreen

@Composable
fun MainScreenNavGraph(
    navController: NavHostController,
    rootNavController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.Home.route,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable(route = Screen.Main.Home.route) {
            HomeScreen(navigateToSignIn = {
                rootNavController.popBackStack()
                rootNavController.navigate(Graph.Root.root)
            })
        }
        composable(route = Screen.Main.Appointment.route) {
            AppointmentScreen()
        }
        composable(route = Screen.Main.Profile.route) {
            ProfileScreen()
        }
        composable(route = Screen.Main.Settings.route) {
            SettingsScreen()
        }
    }
}