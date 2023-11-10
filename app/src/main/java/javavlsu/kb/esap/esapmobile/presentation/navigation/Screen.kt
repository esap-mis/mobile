package javavlsu.kb.esap.esapmobile.presentation.navigation

import javavlsu.kb.esap.esapmobile.R

sealed class Screen(
    val title: String,
    val route: String,
    val icon: Int? = null
) {
    object SignIn: Screen(title = "Вход", route = "signin")

    object SignUp: Screen(title = "Регистрация", route = "signup")

    object Main {
        object Home: Screen(title = "Главная", route = "home", icon = R.drawable.hospital)

        object AppointmentBooking: Screen(title = "Запись", route = "appointment", icon = R.drawable.make_appointments) {

            object Confirmation: Screen(title = "Подтвердить запись", route = "appointment/{selectedDate}/{timeSlot}/{doctorId}")
        }

        object Appointments: Screen(title = "Приемы", route = "appointments", icon = R.drawable.appointments)

        object Results: Screen(title = "Результат", route = "results", icon = R.drawable.results)

        object More: Screen(title = "Еще", route = "more", icon = R.drawable.drawer_menu) {

            object Settings: Screen(title = "Настройки", route = "settings", icon = R.drawable.settings)
        }
    }
}