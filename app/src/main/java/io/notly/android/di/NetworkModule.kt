package io.notly.android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.notly.android.api.AuthInterceptor
import io.notly.android.api.NotesAPI
import io.notly.android.api.UserAPI
import io.notly.android.utils.Constants
import io.notly.android.utils.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }

    @Provides
    fun providesOkhttpClient(authInterceptor: AuthInterceptor) : OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(authInterceptor).build()
    }

    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder) : UserAPI {
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesNotesAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): NotesAPI {
        return retrofitBuilder.client(okHttpClient).build().create(NotesAPI::class.java)
    }

}