package javavlsu.kb.esap.esapmobile.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javavlsu.kb.esap.esapmobile.ui.navigation.*
import javavlsu.kb.esap.esapmobile.ui.screen.*
import javavlsu.kb.esap.esapmobile.ui.theme.EsapMobileTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EsapMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = SignInScreen.route) {
                        composable(SignInScreen.route) {
                            SignInScreen(navController)
                        }
                        composable(SignUpScreen.route) {
                            SignUpScreen(navController)
                        }
                        composable(MainScreen.route) {
                            MainScreen(navController)
                        }
                    }
                }
            }
        }
    }
}