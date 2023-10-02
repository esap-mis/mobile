package javavlsu.kb.esap.esapmobile.presentation.navigation

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
    object SignIn: Screen(title = "Вход", route = "signin", icon = Icons.Default.Home)

    object SignUp: Screen(title = "Регистрация", route = "signup", icon = Icons.Default.Home)

    object Main {
        object Home: Screen(title = "Главная", route = "home", icon = Icons.Default.Home)

        object Profile: Screen(title = "Профиль", route = "profile", icon = Icons.Default.Person)

        object Settings: Screen(title = "Настройки", route = "settings", icon = Icons.Default.Settings)
    }
}