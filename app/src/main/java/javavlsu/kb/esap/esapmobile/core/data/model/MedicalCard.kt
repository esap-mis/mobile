package javavlsu.kb.esap.esapmobile.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MedicalCard(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    var medicalRecord: List<MedicalRecord>,
    var patient: Patient,
)

