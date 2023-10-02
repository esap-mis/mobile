package javavlsu.kb.esap.esapmobile.ui.graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import javavlsu.kb.esap.esapmobile.ui.navigation.ProfileScreen
import javavlsu.kb.esap.esapmobile.ui.navigation.Screen
import javavlsu.kb.esap.esapmobile.ui.navigation.SettingsScreen
import javavlsu.kb.esap.esapmobile.ui.screen.HomeScreen

@Composable
fun MainScreenNavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(navController = navController,
        startDestination = Screen.Main.Home.route,
        modifier = Modifier.padding(paddingValues = paddingValues)
    ) {
        composable(route = Screen.Main.Home.route) {
            HomeScreen()
        }
        composable(route = Screen.Main.Profile.route) {
            ProfileScreen()
        }
        composable(route = Screen.Main.Settings.route) {
            SettingsScreen()
        }
    }
}