package javavlsu.kb.esap.esapmobile.presentation.component.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteMessageCard(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onClickSend: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 6.dp,
        color = Color.White,
        shape = RoundedCornerShape(30.dp),
    ) {
        TextField(
            modifier = Modifier
                .background(color = Color.White),
            value = value,
            onValueChange = { value ->
                onValueChange(value)
            },
            placeholder = {
                Text(text = stringResource(id = R.string.your_message))
            },
            trailingIcon = {
                Image(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable {
                            onClickSend()
                        },
                    painter = painterResource(id = R.drawable.ic_send_message),
                    contentDescription = ""
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedPlaceholderColor = Gray40,
                containerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}