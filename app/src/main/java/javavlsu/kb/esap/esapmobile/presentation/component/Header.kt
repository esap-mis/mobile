package javavlsu.kb.esap.esapmobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import javavlsu.kb.esap.esapmobile.R
import javavlsu.kb.esap.esapmobile.domain.model.UserResponse
import javavlsu.kb.esap.esapmobile.domain.model.response.PatientResponse
import javavlsu.kb.esap.esapmobile.presentation.theme.Gray40
import androidx.compose.material3.Button

@Composable
fun Header(
    user: UserResponse,
    isHome: Boolean = true,
    onToggleAppointments: () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        if (isHome) {
            Text(
                text = "${stringResource(R.string.hello)} ${user.firstName} ${stringResource(R.string.smile)}",
                fontSize = 22.sp,
                fontWeight = FontWeight.W600,
                color = Color.Black,
                textAlign = TextAlign.Left
            )
            SearchIcon()
        } else {
            Text(
                text = "${user.firstName} ${user.lastName}",
                fontSize = 22.sp,
                fontWeight = FontWeight.W600,
                color = Color.Black,
                textAlign = TextAlign.Left
            )
        }
    }
    if (user is PatientResponse) {
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.medical_card),
                tint = Color.Blue,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.medical_record),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Blue,
                textAlign = TextAlign.Left
            )
        }
    }
    if (!isHome) {
        CustomToggleSwitch(onToggle = onToggleAppointments)
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

@Composable
fun CustomToggleSwitch(
    onToggle: () -> Unit
) {
    var isUpcoming by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .clickable {
                isUpcoming = !isUpcoming;
                onToggle()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    isUpcoming = true;
                    onToggle()
                },
                modifier = Modifier
                    .weight(1f)
                    .background(if (isUpcoming) MaterialTheme.colorScheme.primary else Color.Gray)
            ) {
                Text(text = "Предстоящие", color = if (isUpcoming) Color.White else Color.Black)
            }

            Spacer(modifier = Modifier.width(4.dp))

            Button(
                onClick = {
                    isUpcoming = false;
                    onToggle()
                },
                modifier = Modifier
                    .weight(1f)
                    .background(if (!isUpcoming) MaterialTheme.colorScheme.primary else Color.Gray)
            ) {
                Text(text = "Прошедшие", color = if (!isUpcoming) Color.White else Color.Black)
            }
        }
    }
}
