package javavlsu.kb.esap.esapmobile.presentation.component.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import javavlsu.kb.esap.esapmobile.core.navigation.Screen
import javavlsu.kb.esap.esapmobile.presentation.theme.Blue
import org.jetbrains.compose.resources.painterResource

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
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        navigationItems.forEachIndexed { index, screen ->
            val isSelected = (selectedScreen == index)

            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(screen.icon!!),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.W600
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
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            )
        }
    }
}
