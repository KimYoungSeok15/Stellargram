package com.ssafy.instargram.data.remote

import com.ssafy.instargram.InstargramApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL: String =InstargramApplication.INSTARGRAM_APP_URI
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            //json 변화기 Factory
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
            .create(ApiService::class.java)

    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }


    private fun getLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    /*    @Provides
        @Singleton
        fun provideSampleApiService(retrofit: Retrofit): SampleApi {
            return retrofit.create(SampleApi::class.java)
        }*/
}