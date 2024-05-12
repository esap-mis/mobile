package javavlsu.kb.esap.esapmobile.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Clinic(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String,
    val address: String,
    val phoneNumber: String,
    val users: List<User>
)
