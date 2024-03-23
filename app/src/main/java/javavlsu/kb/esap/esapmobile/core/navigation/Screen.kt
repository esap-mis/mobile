package javavlsu.kb.esap.esapmobile.core.navigation

import javavlsu.kb.esap.esapmobile.R

sealed class Screen(
    val title: String,
    val route: String,
    val icon: Int? = null
) {
    object SignIn: Screen(title = "Вход", route = "signin")

    object SignUp: Screen(title = "Регистрация", route = "signup")

    object ForgotPassword: Screen(title = "Забыли пароль", route = "forgot_password")

    object Main {
        object Home: Screen(title = "Главная", route = "home", icon = R.drawable.hospital)

        object AppointmentBooking: Screen(title = "Запись", route = "appointment", icon = R.drawable.make_appointments) {

            object Confirmation: Screen(title = "Подтвердить запись", route = "appointment/{selectedDate}/{scheduleId}/{startTime}/{doctorId}")
        }

        object Doctors: Screen(title = "Врачи", route = "doctors", icon = R.drawable.doctors)

        object Patients: Screen(title = "Пациенты", route = "patients", icon = R.drawable.patients)

        object Appointments: Screen(title = "Приемы", route = "appointments", icon = R.drawable.appointments)

        object Passcode: Screen(title = "Код-пароль", route = "passcode")

        object Results: Screen(title = "Результаты", route = "results", icon = R.drawable.results) {

            object Analysis: Screen(title = "Анализы", route = "results/analysis/{patientId}")

            object Reports: Screen(title = "Заключения врачей", route = "results/report/{patientId}")
        }

        object More: Screen(title = "Еще", route = "more", icon = R.drawable.drawer_menu) {

            object Settings: Screen(title = "Настройки", route = "settings", icon = R.drawable.settings)
        }
    }
}