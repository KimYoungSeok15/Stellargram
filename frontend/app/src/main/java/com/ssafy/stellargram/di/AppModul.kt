package com.ssafy.stellargram.di


import com.ssafy.stellargram.data.db.dao.StarDAO
import com.ssafy.stellargram.data.db.repository.StarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideStarRepository(starDAO: StarDAO): StarRepository {
        return StarRepository(starDAO)
    }

}