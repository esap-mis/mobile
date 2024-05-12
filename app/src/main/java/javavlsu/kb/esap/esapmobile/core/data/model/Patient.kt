package javavlsu.kb.esap.esapmobile.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val firstName: String,
    val patronymic: String,
    val lastName: String,
    val birthDate: LocalDate,
    val address: String,
    val phoneNumber: String,
    val email: String,
    val medicalCard: MedicalCard,
    val appointments: List<Appointment>
)
