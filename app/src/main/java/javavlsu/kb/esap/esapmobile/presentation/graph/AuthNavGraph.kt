package javavlsu.kb.esap.esapmobile.presentation.graph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import javavlsu.kb.esap.esapmobile.presentation.navigation.Screen
import javavlsu.kb.esap.esapmobile.presentation.ui.SignInScreen
import javavlsu.kb.esap.esapmobile.presentation.ui.SignUpScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController
) {
    navigation(
        route = Graph.Auth.root,
        startDestination = Screen.SignIn.route
    ) {
        composable(route = Screen.SignIn.route) {
            SignInScreen(
                navigateToSignUp = { navController.navigate(Screen.SignUp.route) },
                navigateToMain = { navController.navigate(Graph.Main.root) },
                navigateBack = { navController.navigate(Screen.SignIn.route) }
            )
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(
                navigateToSignIn = {
                    navController.popBackStack()
                    navController.navigate(Screen.SignIn.route)
                }
            )
        }
    }
}