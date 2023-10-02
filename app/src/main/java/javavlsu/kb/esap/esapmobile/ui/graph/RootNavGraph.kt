package javavlsu.kb.esap.esapmobile.ui.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import javavlsu.kb.esap.esapmobile.ui.screen.main.MainScreen

@Composable
fun RootNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Graph.Auth.root
    ) {
        authNavGraph(navController = navHostController)
        composable(route = Graph.Main.root) {
            val navController = rememberNavController()
            MainScreen(navHostController = navController)
        }
    }
}