package javavlsu.kb.esap.esapmobile.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import javavlsu.kb.esap.esapmobile.core.data.dao.UserDao
import javavlsu.kb.esap.esapmobile.core.data.model.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}