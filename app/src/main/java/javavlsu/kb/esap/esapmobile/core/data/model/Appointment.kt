package javavlsu.kb.esap.esapmobile.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
class Appointment(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val patient: Patient,
    val doctor: Doctor,
    val date: LocalDate,
    val startAppointments: LocalTime,
    val endAppointments: LocalTime,
    val schedule: Schedule
)