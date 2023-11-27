package javavlsu.kb.esap.esapmobile.core.data

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javavlsu.kb.esap.esapmobile.presentation.data.CalendarUiModel
import javavlsu.kb.esap.esapmobile.presentation.util.CalendarDataSource
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val dataSource: CalendarDataSource,
): BaseViewModel() {

    val calendarData = MutableLiveData<CalendarUiModel>()

    init {
        updateCalendarData()
    }

    fun selectDate(date: LocalDate) {
        val updatedData = calendarData.value?.copy(
            selectedDate = toItemUiModel(date, true),
            visibleDates = calendarData.value?.visibleDates?.map {
                it.copy(
                    isSelected = it.date.isEqual(date)
                )
            } ?: emptyList()
        )
        calendarData.value = updatedData
    }

    fun navigateToPreviousWeek() {
        val newStartDate = calendarData.value?.startDate?.date?.minusWeeks(1)
        updateCalendarData(newStartDate)
    }

    fun navigateToNextWeek() {
        val newStartDate = calendarData.value?.endDate?.date?.plusDays(1)
        updateCalendarData(newStartDate)
    }

    private fun updateCalendarData(startDate: LocalDate? = null) {
        val lastSelectedDate = calendarData.value?.selectedDate?.date ?: LocalDate.now()
        val newStartDate = startDate ?: lastSelectedDate
        val data = dataSource.getData(startDate = newStartDate, lastSelectedDate = lastSelectedDate)
        calendarData.value = data
    }

    private fun toItemUiModel(date: LocalDate, isSelectedDate: Boolean) = CalendarUiModel.Date(
        isSelected = isSelectedDate,
        isToday = date.isEqual(LocalDate.now()),
        date = date
    )
}
