package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import javavlsu.kb.esap.esapmobile.presentation.graph.MainScreenNavGraph
import javavlsu.kb.esap.esapmobile.presentation.navigation.BottomNavigationBar

@Composable
fun MainScreen(
    navHostController: NavHostController,
    rootNavController: NavHostController
) {
    Scaffold(bottomBar = {
        BottomNavigationBar(navController = navHostController)
    }) { paddingValues ->
        MainScreenNavGraph(
            navController = navHostController,
            rootNavController = rootNavController,
            paddingValues = paddingValues
        )
    }
}