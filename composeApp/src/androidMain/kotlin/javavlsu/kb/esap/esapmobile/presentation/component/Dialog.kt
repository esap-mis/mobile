package javavlsu.kb.esap.esapmobile.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import javavlsu.kb.esap.esapmobile.R

@Composable
fun ResponseDialog(responseMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(stringResource(R.string.server_response))
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
                Text(stringResource(R.string.ok))
            }
        }
    )
}