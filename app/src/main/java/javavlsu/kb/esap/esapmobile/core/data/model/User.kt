package javavlsu.kb.esap.esapmobile.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val password: String,
    val login: String,
    val roles: String,
//    val clinic: Clinic,
    val gender: Int = 0
)