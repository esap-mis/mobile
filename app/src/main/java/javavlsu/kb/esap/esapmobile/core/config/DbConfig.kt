package javavlsu.kb.esap.esapmobile.core.config

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javavlsu.kb.esap.esapmobile.core.data.AppDatabase
import javavlsu.kb.esap.esapmobile.core.data.dao.UserDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbConfig {
    @Provides
    @Singleton
    fun provideMainDB(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "esap_mobile.db"
        ).build()
    }

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDAO {
        return appDatabase.userDAO
    }
}