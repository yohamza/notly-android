package io.notly.android.api

import io.notly.android.models.Note
import io.notly.android.models.NoteRequest
import io.notly.android.models.NoteResponse
import io.notly.android.models.NotesListResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    @GET("/notes")
    suspend fun getNotes() : Response<NotesListResponse>

    @GET("/notes/{id}")
    suspend fun getNoteById(@Path("id") id: String) : Response<NoteResponse>

    @POST("/notes")
    suspend fun createNote(@Body note: NoteRequest) : Response<NoteResponse>

    @PUT("/notes/{id}")
    suspend fun updateNote(@Path("id") id: String, @Body note: NoteRequest) : Response<NoteResponse>

    @DELETE("/notes/{id}")
    suspend fun deleteNoteById(@Path("id") id: String) : Response<NoteResponse>
}