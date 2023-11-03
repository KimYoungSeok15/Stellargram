package com.ssafy.stellargram.data.remote

import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.model.CardsResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL: String = StellargramApplication.INSTARGRAM_APP_URI
    //  Dagger Hilt를 통해 Singleton 스코프를 가진 OkHttpClient 인스턴스를 규정
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

    // 날씨 정보 불러오는 API 규정
    @Singleton
    @Provides
    fun provideRetrofitInstanceWeather(
    ): ApiServiceForWeather {
        return Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr")
            //json 변화기 Factory
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
            .create(ApiServiceForWeather::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofitInstanceCards(): ApiServiceForCards {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // 기본 URL을 여기에 설정해야 합니다.
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
            .create(ApiServiceForCards::class.java)
    }
}

