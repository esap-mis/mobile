package javavlsu.kb.esap.esapmobile.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime


@Entity
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val doctor: Doctor,
    val date: LocalDate,
    val startDoctorAppointment: LocalTime,
    val endDoctorAppointment: LocalTime,
    val maxPatientPerDay: Int = 0,
    val appointments: List<Appointment>
)