package javavlsu.kb.esap.esapmobile.ui.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import javavlsu.kb.esap.esapmobile.ui.screen.MainScreen

@Composable
fun RootNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Graphs.AUTH.root
    ) {
        authNavGraph(navController = navHostController)
        composable(route = Graphs.MAIN.root) {
            MainScreen()
        }
    }
}


sealed class Graphs(
    val root: String
) {
    object AUTH : Graphs("auth_graph")
    object MAIN : Graphs("main_graph")
}