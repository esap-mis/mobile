package javavlsu.kb.esap.esapmobile.ui.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import javavlsu.kb.esap.esapmobile.ui.navigation.Screen
import javavlsu.kb.esap.esapmobile.ui.screen.SignInScreen
import javavlsu.kb.esap.esapmobile.ui.screen.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        route = Graphs.AUTH.root,
        startDestination = Screen.SignIn.route
    ) {
        composable(route = Screen.SignIn.route) {
            SignInScreen(navController = navController)
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
    }
}