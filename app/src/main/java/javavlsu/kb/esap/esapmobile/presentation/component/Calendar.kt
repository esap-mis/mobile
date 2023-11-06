package javavlsu.kb.esap.esapmobile.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import javavlsu.kb.esap.esapmobile.data.CalendarViewModel
import javavlsu.kb.esap.esapmobile.presentation.data.CalendarUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarViewModel: CalendarViewModel = hiltViewModel()
) {
    val data by calendarViewModel.calendarData.observeAsState()

    data?.let { calendarData ->
        Column(modifier = modifier.fillMaxWidth(1f)) {
            Header(data = calendarData)
            Content(
                data = calendarData,
                onDateClickListener = { date ->
                    calendarViewModel.selectDate(date.date)
                },
                onPrevClickListener = {
                    calendarViewModel.navigateToPreviousWeek()
                },
                onNextClickListener = {
                    calendarViewModel.navigateToNextWeek()
                }
            )
        }
    }
}

@Composable
fun Header(
    data: CalendarUiModel
) {
    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = data.selectedDate.date.format(
                DateTimeFormatter.ofPattern("MMMM yyyy")
            ),
            fontSize = 18.sp,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun Content(
    data: CalendarUiModel,
    onDateClickListener: (CalendarUiModel.Date) -> Unit,
    onPrevClickListener: (LocalDate) -> Unit,
    onNextClickListener: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onPrevClickListener(data.startDate.date)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = null
            )
        }

        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Fixed(7)
        ) {
            items(data.visibleDates.size) { index ->
                ContentItem(
                    date = data.visibleDates[index],
                    onDateClickListener
                )
            }
        }

        IconButton(onClick = {
            onNextClickListener(data.endDate.date)
        }) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun ContentItem(
    date: CalendarUiModel.Date,
    onClickListener: (CalendarUiModel.Date) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(
                vertical = 2.dp,
                horizontal = 2.dp
            )
            .clickable {
                onClickListener(date)
            }
        ,
        colors = CardDefaults.cardColors(
            containerColor = if (date.isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.White
            }
        ),
    ) {
        Column(
            modifier = Modifier
                .width(48.dp)
                .height(58.dp)
                .padding(2.dp)
        ) {
            Text(
                text = date.day,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.date.dayOfMonth.toString(),
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}