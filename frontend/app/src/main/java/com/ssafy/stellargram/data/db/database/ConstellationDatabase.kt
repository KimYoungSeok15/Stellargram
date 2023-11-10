package com.ssafy.stellargram.data.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssafy.stellargram.data.db.dao.ConstellationDAO
import com.ssafy.stellargram.data.db.entity.Constellation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [Constellation::class], version = 1, exportSchema = true)
abstract class ConstellationDatabase: RoomDatabase() {
    abstract fun constellationDAO(): ConstellationDAO
}


@Module
@InstallIn(SingletonComponent::class)
object ConstellationDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context.applicationContext,
        ConstellationDatabase::class.java,
        "constellations"
    )
        .createFromAsset("StarData.db")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideDao(database: ConstellationDatabase) = database.constellationDAO()
}