package javavlsu.kb.esap.esapmobile.core.navigation

import esapmobile.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource

sealed class Screen(
    val title: String,
    val route: String,
    val icon: DrawableResource? = null
) {
    object SignIn: Screen(title = "Вход", route = "signin")

    object SignUp: Screen(title = "Регистрация", route = "signup")

    object ForgotPassword: Screen(title = "Забыли пароль", route = "forgot_password")

    object Main {
        object Home: Screen(title = "Главная", route = "home", icon = Res.drawable.hospital)

        object AppointmentBooking: Screen(title = "Запись", route = "appointment", icon = Res.drawable.make_appointments) {

            object Confirmation: Screen(title = "Подтвердить запись", route = "appointment/{selectedDate}/{scheduleId}/{startTime}/{doctorId}")
        }

        object Doctors: Screen(title = "Врачи", route = "doctors", icon = Res.drawable.doctors)

        object Patients: Screen(title = "Пациенты", route = "patients", icon = Res.drawable.patients)

        object Appointments: Screen(title = "Приемы", route = "appointments", icon = Res.drawable.appointments)

        object Results: Screen(title = "Результаты", route = "results", icon = Res.drawable.results) {

            object Analysis: Screen(title = "Анализы", route = "results/analysis/{patientId}")

            object Reports: Screen(title = "Заключения врачей", route = "results/report/{patientId}")
        }

        object More: Screen(title = "Еще", route = "more", icon = Res.drawable.drawer_menu) {

            object Settings: Screen(title = "Настройки", route = "settings", icon = Res.drawable.settings)

            object Chat: Screen(title = "Чат", route = "chat", icon = Res.drawable.ic_send_message)
        }
    }
}