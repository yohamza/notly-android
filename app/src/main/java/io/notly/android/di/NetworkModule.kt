package io.notly.android.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.notly.android.features.auth.data.api.AuthInterceptor
import io.notly.android.features.note.data.api.NotesAPI
import io.notly.android.features.auth.data.api.UserAPI
import io.notly.android.features.note.data.data_source.NoteRemoteDataSource
import io.notly.android.features.note.data.repository.NoteRepositoryImpl
import io.notly.android.features.note.domain.repository.NoteRepository
import io.notly.android.features.note.domain.use_case.*
import io.notly.android.utils.Constants.BASE_URL
import io.notly.android.utils.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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

    /**Context**/
    @Singleton
    @Provides
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }


    /**Coroutines**/
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    @Provides
    fun provideCoroutinesScope(dispatcher: DispatcherProvider): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatcher.default)

    }

    @Singleton
    @Provides
    fun providesNoteRepository(remoteDataSource: NoteRemoteDataSource, externalScope: CoroutineScope): NoteRepository {
        return NoteRepositoryImpl(
            remoteDataSource,
            externalScope
        )
    }

    @Singleton
    @Provides
    fun providesNoteUseCases(noteRepository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            GetNotes(noteRepository),
            UpdateNote(noteRepository),
            CreateNote(noteRepository),
            DeleteNoteById(noteRepository)
        )
    }

}