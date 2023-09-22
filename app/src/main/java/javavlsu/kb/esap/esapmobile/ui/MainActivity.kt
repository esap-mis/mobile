package javavlsu.kb.esap.esapmobile.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import javavlsu.kb.esap.esapmobile.data.AuthViewModel
import javavlsu.kb.esap.esapmobile.ui.component.*
import javavlsu.kb.esap.esapmobile.ui.theme.EsapMobileTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EsapMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen(authViewModel)
                }
            }
        }
    }
}