package io.notly.android.api

import io.notly.android.models.Note
import io.notly.android.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesAPI {

    @GET("/notes")
    suspend fun getNotes() : Response<List<NoteResponse>>

    @GET("/notes/{id}")
    suspend fun getNoteById(@Path("id") id: Int) : Response<NoteResponse>

    @POST("/notes")
    suspend fun createNote(@Body note: Note) : Response<NoteResponse>

    @PUT("/notes/{id}")
    suspend fun updateNote(@Path("id") id: Int, @Body note: Note) : Response<NoteResponse>

    @DELETE("/notes")
    suspend fun deleteAllNotes() : Response<NoteResponse>

    @DELETE("/notes/{id}")
    suspend fun deleteNoteById(@Path("id") id: Int) : Response<NoteResponse>
}