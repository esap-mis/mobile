package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import javavlsu.kb.esap.esapmobile.presentation.graph.MainScreenNavGraph
import javavlsu.kb.esap.esapmobile.presentation.navigation.BottomNavigationBar
import javavlsu.kb.esap.esapmobile.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navHostController: NavHostController,
    rootNavController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val items = listOf(Screen.Main.More.Settings)
    val selectedItem = remember { mutableStateOf(items[0]) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModalDrawerSheet {
                        Spacer(Modifier.height(12.dp))
                        items.forEach { item ->
                            NavigationDrawerItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.icon!!),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(30.dp)
                                    )},
                                label = { Text(item.title) },
                                selected = item == selectedItem.value,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    selectedItem.value = item
                                    navHostController.navigate(item.route)
                                },
                                modifier = Modifier
                                    .padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Scaffold(
                        bottomBar = {
                            BottomNavigationBar(
                                navController = navHostController,
                                onMoreButtonClick = {
                                    scope.launch { drawerState.open() }
                                }
                            )
                        }
                    ) { paddingValues ->
                        MainScreenNavGraph(
                            navController = navHostController,
                            rootNavController = rootNavController,
                            paddingValues = paddingValues
                        )
                    }
                }
            }
        )
    }
}