package javavlsu.kb.esap.esapmobile.ui.navigation

sealed class NavigationItem(
    val route: String
)

object SignInScreen: NavigationItem("signin")

object SignUpScreen: NavigationItem("signup")

object MainScreen: NavigationItem("main")