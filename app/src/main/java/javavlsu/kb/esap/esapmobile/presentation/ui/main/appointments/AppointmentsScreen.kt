package javavlsu.kb.esap.esapmobile.presentation.ui.main.appointments

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


// TODO: Вынести запрос с отображением информации о пользователе вотедльный компонент, чтобы рендерить на нескольких страницах
@Composable
fun AppointmentsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Приемы")
    }
}