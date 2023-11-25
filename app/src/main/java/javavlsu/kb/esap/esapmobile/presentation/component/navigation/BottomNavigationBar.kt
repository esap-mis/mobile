package javavlsu.kb.esap.esapmobile.presentation.component.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import javavlsu.kb.esap.esapmobile.core.navigation.Screen
import javavlsu.kb.esap.esapmobile.presentation.theme.Blue

@Composable
fun BottomNavigationBar(
    userRoles: String,
    navController: NavController,
    onMoreButtonClick: () -> Unit
) {
    val patientNavigationItems = listOf(
        Screen.Main.Home,
        Screen.Main.AppointmentBooking,
        Screen.Main.Appointments,
        Screen.Main.Results,
        Screen.Main.More
    )
    val doctorNavigationItems = listOf(
        Screen.Main.Home,
        Screen.Main.Appointments,
        Screen.Main.Patients,
        Screen.Main.Doctors,
        Screen.Main.More.Settings
    )

    val navigationItems = if (userRoles.contains("ROLE_PATIENT")) patientNavigationItems else doctorNavigationItems
    var selectedScreen by remember { mutableIntStateOf(0) }

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
            val destinationRoute = destination.route ?: ""
            val index = navigationItems.indexOfFirst { it.route == destinationRoute }
            if (index != -1 && selectedScreen != index) {
                selectedScreen = index
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    NavigationBar(
        containerColor = Color.White
    ) {
        navigationItems.forEachIndexed { index, screen ->
            val isSelected = (selectedScreen == index)
            val color = if (isSelected) Blue else Color.Gray

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon!!),
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W600,
                        color = color,
                    )
                },
                selected = isSelected,
                onClick = {
                    if (navController.currentBackStack.value.size >= 2) {
                        navController.popBackStack()
                    }
                    selectedScreen = index
                    if (selectedScreen == 4) {
                        onMoreButtonClick()
                    } else {
                        navController.navigate(screen.route)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White
                )
            )
        }
    }
}
