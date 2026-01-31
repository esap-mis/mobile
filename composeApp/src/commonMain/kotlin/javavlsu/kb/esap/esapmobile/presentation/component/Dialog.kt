package javavlsu.kb.esap.esapmobile.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import esapmobile.composeapp.generated.resources.Res
import esapmobile.composeapp.generated.resources.ok
import esapmobile.composeapp.generated.resources.server_response
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResponseDialog(responseMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(stringResource(Res.string.server_response))
        },
        text = {
            Text(responseMessage)
        },
        confirmButton = {
            androidx.compose.material3.Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue),
            ) {
                Text(stringResource(Res.string.ok))
            }
        }
    )
}