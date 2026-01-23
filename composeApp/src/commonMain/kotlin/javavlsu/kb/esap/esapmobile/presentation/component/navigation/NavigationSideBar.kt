package javavlsu.kb.esap.esapmobile.presentation.component.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import javavlsu.kb.esap.esapmobile.core.navigation.Screen
import org.jetbrains.compose.resources.painterResource

@Composable
fun NavigationSideBar(
    items: List<Screen>,
    navController: NavController,
    onMoreButtonClick: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationRail(
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items.forEach { item ->
                NavigationRailItem(
                    icon = {
                        Icon(
                            painter = painterResource(item.icon!!),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text(item.title) },
                    selected = currentRoute == item.route,
                    onClick = { 
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            NavigationRailItem(
                icon = { 
                    Icon(
                        painter = painterResource(Screen.Main.More.icon!!), 
                        contentDescription = Screen.Main.More.title,
                        modifier = Modifier.size(24.dp)
                    ) 
                },
                label = { Text(Screen.Main.More.title) },
                selected = false,
                onClick = onMoreButtonClick
            )
        }
    }
}