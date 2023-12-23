package javavlsu.kb.esap.esapmobile.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import javavlsu.kb.esap.esapmobile.core.navigation.graph.RootNavHost
import javavlsu.kb.esap.esapmobile.presentation.theme.EsapMobileTheme

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
                    RootNavHost(navHostController = navController)
                }
            }
        }
    }

//    private suspend fun sendTokenToServer(token: String?) {
//        token?.let {
//            try {
//                val response: Response<String> = notificationApiService.registerToken(TokenRequest(token))
//
//                if (response.isSuccessful) {
//                    Log.d("TOKEN", "FCM Token sent to server successfully")
//                } else {
//                    Log.e("TOKEN", "Failed to send FCM Token to server: ${response.message()}")
//                }
//            } catch (e: Exception) {
//                Log.e("TOKEN", "Exception during FCM Token registration: ${e.message}", e)
//            }
//        }
//    }
}