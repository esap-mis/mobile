package javavlsu.kb.esap.esapmobile.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import javavlsu.kb.esap.esapmobile.core.data.dao.UserDAO
import javavlsu.kb.esap.esapmobile.core.data.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDAO: UserDAO
}