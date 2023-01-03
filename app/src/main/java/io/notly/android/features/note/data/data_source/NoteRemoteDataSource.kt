package io.notly.android.features.note.data.data_source

import io.notly.android.core.NetworkResult
import io.notly.android.core.NetworkResult.*
import io.notly.android.features.note.data.api.NotesAPI
import io.notly.android.features.note.domain.model.Note
import io.notly.android.features.note.domain.model.NoteResponse
import io.notly.android.features.note.domain.model.NotesListResponse
import io.notly.android.utils.getErrorMessage
import io.notly.android.utils.nonNullMessage
import okio.IOException
import javax.inject.Inject

class NoteRemoteDataSource @Inject constructor(private val notesAPI: NotesAPI) {

    suspend fun getNotes(): NetworkResult<NotesListResponse> {

        return try {
            val response = notesAPI.getNotes()

            if(response.isSuccessful){
                Success(response.body()!!)
            }
            else if(response.errorBody() != null){
                Error(response.errorBody()!!.getErrorMessage())
            }
            else{
                Error(response.errorBody()!!.getErrorMessage())
            }
        } catch (exception: IOException){
            Error(exception.nonNullMessage())
        }
    }

    suspend fun createNote(note: Note): NetworkResult<NoteResponse> {
        return try {

            val response = notesAPI.createNote(note)

            if(response.isSuccessful){
                Success(response.body()!!)
            }
            else if(response.errorBody() != null){
                Error(response.errorBody()!!.getErrorMessage())
            }
            else{
                Error(response.errorBody()!!.getErrorMessage())
            }

        } catch (exception: IOException) {
            Error(exception.nonNullMessage())
        }
    }

    suspend fun updateNote(id: String, note: Note): NetworkResult<NoteResponse>{

        return try {
            val response = notesAPI.updateNote(id, note)

            if(response.isSuccessful){
                Success(response.body()!!)
            }
            else if(response.errorBody() != null){
                Error(response.errorBody()!!.getErrorMessage())
            }
            else{
                Error(response.errorBody()!!.getErrorMessage())
            }
        }catch (exception: IOException){
            Error(exception.nonNullMessage())
        }
    }

    suspend fun deleteNoteById(id: String) : NetworkResult<NoteResponse>{

        return try {
            val response = notesAPI.deleteNoteById(id)

            if(response.isSuccessful){
                Success(response.body()!!)
            }
            else if(response.errorBody() != null){
                Error(response.errorBody()!!.getErrorMessage())
            }
            else{
                Error(response.errorBody()!!.getErrorMessage())
            }
        }catch (exception: IOException){
            Error(exception.nonNullMessage())
        }
    }

}