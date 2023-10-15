package javavlsu.kb.esap.esapmobile.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val title: String,
    val route: String,
    val icon: ImageVector? = null
) {
    object SignIn: Screen(title = "Вход", route = "signin")

    object SignUp: Screen(title = "Регистрация", route = "signup")

    object Main {
        object Home: Screen(title = "Главная", route = "home", icon = Icons.Outlined.Home)

        object Appointment: Screen(title = "Запись", route = "appointment", icon = Icons.Outlined.Schedule)

        object Profile: Screen(title = "Профиль", route = "profile", icon = Icons.Outlined.Person)

        object Settings: Screen(title = "Настройки", route = "settings", icon = Icons.Outlined.Settings)
    }
}