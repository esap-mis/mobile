package javavlsu.kb.esap.esapmobile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import javavlsu.kb.esap.esapmobile.core.data.SettingsViewModel
import javavlsu.kb.esap.esapmobile.core.navigation.graph.RootNavHost
import javavlsu.kb.esap.esapmobile.presentation.theme.EsapMobileTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val isDarkMode by settingsViewModel.isDarkMode.collectAsState()
    val darkTheme = isDarkMode ?: isSystemInDarkTheme()

    EsapMobileTheme(darkTheme = darkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            RootNavHost(navHostController = navController)
        }
    }
}