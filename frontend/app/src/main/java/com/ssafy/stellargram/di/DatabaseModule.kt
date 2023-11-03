package com.ssafy.stellargram.di


//import android.content.Context
//import androidx.room.Room
//import com.ssafy.stellargram.data.db.dao.StarDAO
//import com.ssafy.stellargram.data.db.database.StarDatabase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//private object DatabaseModule {
//
//    @Provides
//    fun provideStarDAO(appDatabase: StarDatabase): StarDAO {
//        return appDatabase.starDao()
//    }
//
//    @Provides
//    @Singleton
//    fun provideAppDatabase(@ApplicationContext context: Context): StarDatabase {
//        return Room.databaseBuilder(
//            context.applicationContext,
//            StarDatabase::class.java,
//            "appDB"
//        ).build()
//    }
//
//}