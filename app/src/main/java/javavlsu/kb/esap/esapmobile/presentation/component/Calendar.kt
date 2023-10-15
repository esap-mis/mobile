package javavlsu.kb.esap.esapmobile.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import javavlsu.kb.esap.esapmobile.presentation.data.CalendarUiModel
import javavlsu.kb.esap.esapmobile.presentation.util.CalendarDataSource
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
) {
    val dataSource = CalendarDataSource()
    var data by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
    Column(modifier = modifier.fillMaxSize()) {
        Header(data = data)
        Content(
            data = data,
            onDateClickListener = { date ->
                data = data.copy(
                    selectedDate = date,
                    visibleDates = data.visibleDates.map {
                        it.copy(
                            isSelected = it.date.isEqual(date.date)
                        )
                    }
                )
            },
            onPrevClickListener = { startDate ->
                val finalStartDate = startDate.minusDays(1)
                data = dataSource.getData(startDate = finalStartDate, lastSelectedDate = data.selectedDate.date)
            },
            onNextClickListener = { endDate ->
                val finalStartDate = endDate.plusDays(2)
                data = dataSource.getData(startDate = finalStartDate, lastSelectedDate = data.selectedDate.date)
            }
        )
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
                contentDescription = "Back"
            )
        }

        LazyVerticalGrid(
            modifier = Modifier.weight(1f),
            columns = GridCells.Adaptive(minSize = 40.dp)
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
                contentDescription = "Next"
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
            .padding(vertical = 4.dp, horizontal = 4.dp)
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
                .width(58.dp)
                .height(58.dp)
                .padding(6.dp)
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