package javavlsu.kb.esap.esapmobile.presentation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import javavlsu.kb.esap.esapmobile.presentation.ui.main.MainScreen

@Composable
fun RootNavHost(
    navHostController: NavHostController
) {
    NavHost(
        route = Graph.Root.root,
        navController = navHostController,
        startDestination = Graph.Auth.root
    ) {
        authNavGraph(navController = navHostController)
        composable(route = Graph.Main.root) {
            val navController = rememberNavController()
            MainScreen(
                navHostController = navController,
                onLogoutClick = {
                    navHostController.popBackStack()
                    navHostController.navigate(Graph.Root.root)
                }
            )
        }
    }
}