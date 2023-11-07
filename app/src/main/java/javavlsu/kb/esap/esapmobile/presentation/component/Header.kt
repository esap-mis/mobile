package javavlsu.kb.esap.esapmobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.domain.model.UserResponse
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40

@Composable
fun Header(
    user: UserResponse,
) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${stringResource(R.string.hello)} ${user.firstName} ${stringResource(R.string.smile)}",
            fontSize = 22.sp,
            fontWeight = FontWeight.W600,
            color = Color.Black,
            textAlign = TextAlign.Left
        )
        SearchIcon()
    }
}

@Composable
private fun SearchIcon() {
    Box(
        modifier = Modifier
            .size(42.dp)
            .background(color = Gray40, shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            tint = Color.Gray,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)
        )
    }
}