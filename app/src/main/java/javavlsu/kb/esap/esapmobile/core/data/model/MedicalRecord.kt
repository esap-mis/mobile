package javavlsu.kb.esap.esapmobile.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class MedicalRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val record: String,
    val fioAndSpecializationDoctor: String,
    val date: LocalDate,
    val analyzes: List<Analysis>,
    val medicalCard: MedicalCard,
)
