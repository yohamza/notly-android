package io.notly.android.features.note.data.api

import io.notly.android.features.note.domain.model.Note
import io.notly.android.features.note.domain.model.NoteResponse
import io.notly.android.features.note.domain.model.NotesListResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    @GET("/notes")
    suspend fun getNotes() : Response<NotesListResponse>

    @GET("/notes/{id}")
    suspend fun getNoteById(@Path("id") id: String) : Response<NoteResponse>

    @POST("/notes")
    suspend fun createNote(@Body note: Note) : Response<NoteResponse>

    @PUT("/notes/{id}")
    suspend fun updateNote(@Path("id") id: String, @Body note: Note) : Response<NoteResponse>

    @DELETE("/notes/{id}")
    suspend fun deleteNoteById(@Path("id") id: String) : Response<NoteResponse>
}