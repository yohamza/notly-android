package io.notly.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.notly.android.api.NotesAPI
import io.notly.android.models.Note
import io.notly.android.models.NoteRequest
import io.notly.android.models.NoteResponse
import io.notly.android.models.NotesListResponse
import io.notly.android.utils.Constants.STANDARD_ERROR
import io.notly.android.utils.NetworkResult
import io.notly.android.utils.NetworkResult.*
import io.notly.android.utils.getErrorMessage
import okio.IOException
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesAPI: NotesAPI) {

    private val _notesListLiveData = MutableLiveData<NetworkResult<NotesListResponse>>()
    val notesListLiveData: LiveData<NetworkResult<NotesListResponse>>
    get() = _notesListLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData: LiveData<NetworkResult<String>>
    get() = _statusLiveData

    private val _noteLiveData = MutableLiveData<NetworkResult<NoteResponse>>()
    val noteLiveData: LiveData<NetworkResult<NoteResponse>>
    get() = _noteLiveData

    suspend fun getNotes(){
        _notesListLiveData.postValue(Loading())
        try {
            val response = notesAPI.getNotes()

            if(response.isSuccessful){
                _notesListLiveData.postValue(Success(response.body()!!))
            }
            else if(response.errorBody() != null){
                _notesListLiveData.postValue(Error(response.errorBody()!!.getErrorMessage()))
            }
            else{
                _notesListLiveData.postValue(Error(response.errorBody()!!.getErrorMessage()))
            }
        }catch (exception: IOException){
            val errorMessage = exception.localizedMessage ?: STANDARD_ERROR
            _notesListLiveData.postValue(Error(errorMessage))
        }

    }

    suspend fun getNoteById(id: String){
        _noteLiveData.postValue(Loading())

        try {
            val response = notesAPI.getNoteById(id)

            if(response.isSuccessful){
                _noteLiveData.postValue(Success(response.body()!!))
            }
            else if(response.errorBody() != null){
                _noteLiveData.postValue(Error(response.errorBody()!!.getErrorMessage()))
            }
            else{
                _noteLiveData.postValue(Error(response.errorBody()!!.getErrorMessage()))
            }
        }catch (exception: IOException){
            val errorMessage = exception.localizedMessage ?: STANDARD_ERROR
            _noteLiveData.postValue(Error(errorMessage))
        }
    }

    suspend fun createNote(note: NoteRequest){
        _noteLiveData.postValue(Loading())

        try {
            val response = notesAPI.createNote(note)

            if(response.isSuccessful){
                _noteLiveData.postValue(Success(response.body()!!))
            }
            else if(response.errorBody() != null){
                _noteLiveData.postValue(Error(response.errorBody()!!.getErrorMessage()))
            }
            else{
                _noteLiveData.postValue(Error(response.errorBody()!!.getErrorMessage()))
            }
        }catch (exception: IOException){
            val errorMessage = exception.localizedMessage ?: STANDARD_ERROR
            _noteLiveData.postValue(Error(errorMessage))
        }
    }

    suspend fun updateNote(id: String, note: NoteRequest){
        _noteLiveData.postValue(Loading())

        try {
            val response = notesAPI.updateNote(id, note)

            if(response.isSuccessful){
                _noteLiveData.postValue(Success(response.body()!!))
            }
            else if(response.errorBody() != null){
                _noteLiveData.postValue(Error(response.errorBody()!!.getErrorMessage()))
            }
            else{
                _noteLiveData.postValue(Error(response.errorBody()!!.getErrorMessage()))
            }
        }catch (exception: IOException){
            val errorMessage = exception.localizedMessage ?: STANDARD_ERROR
            _noteLiveData.postValue(Error(errorMessage))
        }
    }

    suspend fun deleteNoteById(id: String){
        _statusLiveData.postValue(Loading())

        try {
            val response = notesAPI.deleteNoteById(id)

            if(response.isSuccessful){
                _statusLiveData.postValue(Success("Note Deleted Successfully"))
            }
            else if(response.errorBody() != null){
                _statusLiveData.postValue(Error("Something went wrong"))
            }
            else{
                _statusLiveData.postValue(Error("Something went wrong"))
            }
        }catch (exception: IOException){
            val errorMessage = exception.localizedMessage ?: STANDARD_ERROR
            _statusLiveData.postValue(Error(errorMessage))
        }
    }
}