package javavlsu.kb.esap.esapmobile.presentation.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.data.TokenViewModel
import javavlsu.kb.esap.esapmobile.presentation.component.CustomButton
import javavlsu.kb.esap.esapmobile.presentation.graph.MainScreenNavGraph
import javavlsu.kb.esap.esapmobile.presentation.navigation.BottomNavigationBar
import javavlsu.kb.esap.esapmobile.presentation.navigation.Screen
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navHostController: NavHostController,
    tokenViewModel: TokenViewModel = hiltViewModel(),
    onLogoutClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val items = listOf(Screen.Main.More.Settings, "Logout")
    val selectedItem = remember { mutableStateOf(items[0]) }
    val roles by tokenViewModel.roles.observeAsState()

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
                                        .padding(12.dp),
                                    shape = MaterialTheme.shapes.medium,
                                )
                            } else {
                                Column(
                                    modifier = Modifier
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    CustomButton(
                                        text = stringResource(R.string.logout),
                                        color = Color.Red,
                                        onClick = {
                                            tokenViewModel.deleteToken()
                                            tokenViewModel.deleteRoles()
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
                    Scaffold(
                        bottomBar = {
                            roles?.let {
                                BottomNavigationBar(
                                    userRoles = it,
                                    navController = navHostController,
                                    onMoreButtonClick = {
                                        scope.launch { drawerState.open() }
                                    }
                                )
                            }
                        }
                    ) { paddingValues ->
                        MainScreenNavGraph(
                            navController = navHostController,
                            paddingValues = paddingValues
                        )
                    }
                }
            }
        )
    }
}