package org.saudigitus.quicknotification.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import org.saudigitus.quicknotification.data.remote.Basic64AuthInterceptor
import org.saudigitus.quicknotification.data.local.PreferenceProvider
import org.saudigitus.quicknotification.data.remote.EndPoints.DEV_BASE_URL
import org.saudigitus.quicknotification.data.remote.MessagingService
import org.saudigitus.quicknotification.data.repository.MessagingRepository
import org.saudigitus.quicknotification.data.repository.MessagingRepositoryImpl
import org.saudigitus.quicknotification.data.repository.PreferenceProviderRepository
import org.saudigitus.quicknotification.ui.util.Constants.SERVER_URL
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesPreferenceProvider(@ApplicationContext appContext: Context): PreferenceProvider =
        PreferenceProviderRepository(appContext)

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(Basic64AuthInterceptor)
        .connectTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .readTimeout(5, TimeUnit.MINUTES)
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(
        preferenceProvider: PreferenceProvider,
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl("${preferenceProvider.getString(SERVER_URL) ?: DEV_BASE_URL}/api/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun providesMessagingService(
        retrofit: Retrofit
    ): MessagingService = retrofit.create(MessagingService::class.java)

    @Provides
    @Singleton
    fun providesMessagingRepository(
        messagingService: MessagingService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): MessagingRepository = MessagingRepositoryImpl(messagingService, ioDispatcher)
}