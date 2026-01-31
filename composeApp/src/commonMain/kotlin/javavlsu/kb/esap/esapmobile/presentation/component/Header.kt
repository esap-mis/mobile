package javavlsu.kb.esap.esapmobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import esapmobile.composeapp.generated.resources.*
import javavlsu.kb.esap.esapmobile.core.domain.model.UserResponse
import javavlsu.kb.esap.esapmobile.core.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun Header(
    user: UserResponse,
    isHome: Boolean = true,
    onMedicalCardClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (isHome) {
                Text(
                    text = "${stringResource(Res.string.hello)} ${user.firstName} ${stringResource(Res.string.smile)}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Left
                )
            } else {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Left
                )
            }
            SearchIcon()
        }
        if (user is PatientResponse) {
            Spacer(modifier = Modifier.size(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMedicalCardClick() },
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(Res.drawable.medical_card),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.medical_record),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}

@Composable
private fun SearchIcon() {
    Box(
        modifier = Modifier
            .size(42.dp)
            .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)
        )
    }
}
