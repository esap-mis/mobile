package javavlsu.kb.esap.esapmobile.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Analysis(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val result: String,
    val date: LocalDateTime,
    val medicalRecord: MedicalRecord,
)

