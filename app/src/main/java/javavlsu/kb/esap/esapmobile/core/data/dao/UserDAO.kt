package javavlsu.kb.esap.esapmobile.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import javavlsu.kb.esap.esapmobile.core.data.model.User

@Dao
interface UserDAO {
    @Insert
    suspend fun insert(entity: User)

    @Update
    suspend fun update(entity: User)

    @Delete
    suspend fun delete(entity: User)

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>
}