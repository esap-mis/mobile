package javavlsu.kb.esap.esapmobile.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Doctor(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val firstName: String,
    val patronymic: String,
    val lastName: String,
    val specialization: String,
    val schedules: List<Schedule>,
    val appointments: List<Appointment>
)

