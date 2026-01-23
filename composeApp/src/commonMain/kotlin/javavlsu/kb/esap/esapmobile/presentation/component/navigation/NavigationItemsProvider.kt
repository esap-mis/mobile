package javavlsu.kb.esap.esapmobile.presentation.component.navigation

import javavlsu.kb.esap.esapmobile.core.navigation.Screen

object NavigationItemsProvider {
    fun getItems(userRole: String): List<Screen> {
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
}