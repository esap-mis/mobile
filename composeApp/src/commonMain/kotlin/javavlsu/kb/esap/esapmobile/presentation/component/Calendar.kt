package javavlsu.kb.esap.esapmobile.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import javavlsu.kb.esap.esapmobile.core.data.CalendarViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import org.koin.compose.viewmodel.koinViewModel
import java.time.LocalDate

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    calendarViewModel: CalendarViewModel = koinViewModel()
) {
    val selectedDate by calendarViewModel.selectedDate.collectAsState()
    
    val currentMonth = remember {
        val now = java.time.LocalDate.now()
        val m = kotlinx.datetime.Month.entries[now.monthValue - 1]
        YearMonth(now.year, m)
    }
    
    val startMonth = remember {
        var y = currentMonth.year
        var mNum = currentMonth.month.number - 12
        if (mNum < 1) { mNum += 12; y -= 1 }
        val m = kotlinx.datetime.Month.entries[mNum - 1]
        YearMonth(y, m)
    }
    val endMonth = remember {
        var y = currentMonth.year
        var mNum = currentMonth.month.number + 12
        if (mNum > 12) { mNum -= 12; y += 1 }
        val m = kotlinx.datetime.Month.entries[mNum - 1]
        YearMonth(y, m)
    }
    val daysOfWeek = remember { daysOfWeek() }

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )
    
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxWidth()) {
        CalendarHeader(
            calendarMonth = state.lastVisibleMonth,
            onPrevClick = {
                coroutineScope.launch {
                    val current = state.lastVisibleMonth.yearMonth
                    var y = current.year
                    var mNum = current.month.number - 1
                    if (mNum < 1) { mNum = 12; y -= 1 }
                    val m = kotlinx.datetime.Month.entries[mNum - 1]
                    state.animateScrollToMonth(YearMonth(y, m))
                }
            },
            onNextClick = {
                coroutineScope.launch {
                    val current = state.lastVisibleMonth.yearMonth
                    var y = current.year
                    var mNum = current.month.number + 1
                    if (mNum > 12) { mNum = 1; y += 1 }
                    val m = kotlinx.datetime.Month.entries[mNum - 1]
                    state.animateScrollToMonth(YearMonth(y, m))
                }
            }
        )
        
        val week = remember(state.lastVisibleMonth, selectedDate) {
            val lastVisibleMonth = state.lastVisibleMonth
            lastVisibleMonth.weekDays.firstOrNull { week ->
                week.any { day -> 
                    day.date.year == selectedDate.year && 
                    day.date.monthNumber == selectedDate.monthValue && 
                    day.date.dayOfMonth == selectedDate.dayOfMonth 
                }
            } ?: lastVisibleMonth.weekDays.first()
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            for (day in week) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .padding(2.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = if (selectedDate.dayOfMonth == day.date.dayOfMonth && 
                                               selectedDate.monthValue == day.date.monthNumber && 
                                               selectedDate.year == day.date.year) 
                                               MaterialTheme.colorScheme.primary else Color.Transparent)
                        .clickable {
                            calendarViewModel.selectDate(
                                LocalDate.of(day.date.year, day.date.monthNumber, day.date.dayOfMonth)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = day.date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selectedDate.dayOfMonth == day.date.dayOfMonth && 
                                       selectedDate.monthValue == day.date.monthNumber && 
                                       selectedDate.year == day.date.year) 
                                       Color.White else Color.Gray
                        )
                        Text(
                            text = day.date.dayOfMonth.toString(),
                            color = if (selectedDate.dayOfMonth == day.date.dayOfMonth && 
                                       selectedDate.monthValue == day.date.monthNumber && 
                                       selectedDate.year == day.date.year) 
                                       Color.White else Color.Unspecified,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader(
    calendarMonth: CalendarMonth,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrevClick) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous")
        }
        val monthName = calendarMonth.yearMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }
        Text(
            text = "$monthName ${calendarMonth.yearMonth.year}",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onNextClick) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Next")
        }
    }
}