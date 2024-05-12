package javavlsu.kb.esap.esapmobile.core.data.dao

import androidx.room.Dao
import androidx.room.Query
import javavlsu.kb.esap.esapmobile.core.data.model.User

@Dao
interface UserDao : BaseDao<User> {
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>
}