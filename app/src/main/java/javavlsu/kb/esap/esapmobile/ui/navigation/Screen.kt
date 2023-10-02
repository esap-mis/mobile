package javavlsu.kb.esap.esapmobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val title: String,
    val route: String,
    val icon: ImageVector
) {
    object SignIn: Screen(title = "SignIn", route = "signin", icon = Icons.Default.Home)

    object SignUp: Screen(title = "SignUp", route = "signup", icon = Icons.Default.Home)

    object Main {
        object Home: Screen(title = "Home", route = "home", icon = Icons.Default.Home)

        object Profile: Screen(title = "Profile", route = "profile", icon = Icons.Default.Person)

        object Settings: Screen(title = "Settings", route = "settings", icon = Icons.Default.Settings)
    }
}