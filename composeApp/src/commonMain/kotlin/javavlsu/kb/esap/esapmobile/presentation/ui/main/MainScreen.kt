package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.logout
import javavlsu.kb.esap.esapmobile.core.data.TokenViewModel
import javavlsu.kb.esap.esapmobile.core.navigation.Screen
import javavlsu.kb.esap.esapmobile.core.navigation.graph.MainScreenNavGraph
import javavlsu.kb.esap.esapmobile.core.util.Platform
import javavlsu.kb.esap.esapmobile.core.util.getPlatform
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.component.navigation.BottomNavigationBar
import javavlsu.kb.esap.esapmobile.presentation.component.navigation.NavigationItemsProvider
import javavlsu.kb.esap.esapmobile.presentation.component.navigation.NavigationSideBar
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    navHostController: NavHostController,
    tokenViewModel: TokenViewModel = koinViewModel(),
    onLogoutClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val items = listOf(
        Screen.Main.More.Chat,
        Screen.Main.More.Settings,
        "Logout"
    )
    val selectedItem = remember { mutableStateOf(items[0]) }
    val roles by tokenViewModel.roles.collectAsState()
    val platform = getPlatform()
    val navigationItems = roles?.let { NavigationItemsProvider.getItems(it) } ?: emptyList()

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    ModalDrawerSheet {
                        items.forEach { item ->
                            if (item is Screen) {
                                NavigationDrawerItem(
                                    icon = {
                                        Icon(
                                            painter = painterResource(item.icon!!),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(30.dp)
                                        )
                                    },
                                    label = { Text(item.title) },
                                    selected = item == selectedItem.value,
                                    onClick = {
                                        scope.launch { drawerState.close() }
                                        selectedItem.value = item
                                        navHostController.navigate(item.route)
                                    },
                                    modifier = Modifier
                                        .padding(12.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    colors = NavigationDrawerItemDefaults.colors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            } else {
                                Column(
                                    modifier = Modifier
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    CustomButton(
                                        text = stringResource(Res.string.logout),
                                        color = Color.Red,
                                        onClick = {
                                            tokenViewModel.logout()
                                            onLogoutClick()
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            },
            content = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    if (platform == Platform.Android) {
                        Scaffold(
                            bottomBar = {
                                BottomNavigationBar(
                                    items = navigationItems,
                                    navController = navHostController,
                                    onMoreButtonClick = {
                                        scope.launch { drawerState.open() }
                                    }
                                )
                            }
                        ) { paddingValues ->
                            MainScreenNavGraph(
                                navController = navHostController,
                                paddingValues = paddingValues
                            )
                        }
                    } else {
                        Surface(color = MaterialTheme.colorScheme.surface) {
                            Row {
                                NavigationSideBar(
                                    items = navigationItems,
                                    navController = navHostController,
                                    onMoreButtonClick = {
                                        scope.launch { drawerState.open() }
                                    }
                                )
                                MainScreenNavGraph(
                                    navController = navHostController,
                                    paddingValues = PaddingValues()
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}