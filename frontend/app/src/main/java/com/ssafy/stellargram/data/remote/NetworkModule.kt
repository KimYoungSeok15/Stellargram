package com.ssafy.stellargram.data.remote

import android.util.Log
import com.ssafy.stellargram.StellargramApplication
import com.ssafy.stellargram.StellargramApplication.Companion.INSTARGRAM_APP_URI
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.Buffer
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL: String = INSTARGRAM_APP_URI
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
    @Provides
    @Singleton
    fun provideHttpClientHeader(interceptor: MyIdInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
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
            .client(provideHttpClientHeader(MyIdInterceptor()))
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

    class MyIdInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val myId = StellargramApplication.prefs.getString("myId","")
            Log.d("HEADER",myId)
            val newRequest = request().newBuilder()
                .addHeader("myId", myId)
                .build()
            proceed(newRequest)
        }
    }

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
    fun provideRetrofitInstanceAstronomicalEvents(): ApiServiceForAstronomicalEvents {
        return Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/")
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
            .create(ApiServiceForAstronomicalEvents::class.java)
    }

    object RetrofitClient {
        private var instance: Retrofit? = null

        fun getInstance(): Retrofit {
            if (instance == null) {
                instance = Retrofit.Builder()
                    .baseUrl("http://apis.data.go.kr/")
                    .client(provideHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                        TikXmlConverterFactory.create(
                            TikXml.Builder().exceptionOnUnreadXml(false).build()
                        )
                    )
                    .build()
            }
            return instance!!
        }
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

    // 채팅 관련 API
    @Singleton
    @Provides
    fun provideRetrofitInstanceChat(): ApiServiceForChat {
        return Retrofit.Builder()
            .baseUrl(BASE_URL) // 기본 URL을 여기에 설정해야 합니다.
            .client(provideHttpClient())
            .addConverterFactory(provideConverterFactory())
            .build()
            .create(ApiServiceForChat::class.java)
    }

}
