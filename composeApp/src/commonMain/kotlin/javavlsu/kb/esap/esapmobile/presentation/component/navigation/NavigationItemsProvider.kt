package javavlsu.kb.esap.esapmobile.presentation.component.navigation

import javavlsu.kb.esap.esapmobile.core.navigation.Screen

object NavigationItemsProvider {
    fun getNavigationItems(userRole: String): List<Screen> {
        val patientNavigationItems = listOf(
            Screen.Main.Home,
            Screen.Main.AppointmentBooking,
            Screen.Main.Appointments,
            Screen.Main.Results
        )
        val doctorNavigationItems = listOf(
            Screen.Main.Home,
            Screen.Main.Appointments,
            Screen.Main.Patients,
            Screen.Main.Doctors
        )

        return if (userRole == "ROLE_PATIENT") patientNavigationItems else doctorNavigationItems
    }

    fun getMoreItems(userRole: String): List<Any> {
        val list = mutableListOf<Any>()
        if (userRole.contains("ROLE_PATIENT")) {
            list.add(Screen.Main.More.Chat)
        }
        list.add(Screen.Main.More.Settings)
        list.add("Logout")

        return list
    }
}