package io.notly.android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.notly.android.models.Note
import io.notly.android.models.NoteResponse
import io.notly.android.repository.NoteRepository
import io.notly.android.utils.NetworkResult
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository): ViewModel() {

    val notesListLiveData get() = noteRepository.notesListLiveData
    val statusLiveData get() = noteRepository.statusLiveData
    val noteLiveData get() = noteRepository.noteLiveData

    fun getNotes(){
        viewModelScope.launch {
            noteRepository.getNotes()
        }
    }

    fun getNoteById(id: Int){
        viewModelScope.launch {
            noteRepository.getNoteById(id)
        }
    }

    fun createNote(note: Note){
        viewModelScope.launch {
            noteRepository.createNote(note)
        }
    }

    fun editNote(id: Int, note: Note){
        viewModelScope.launch {
            noteRepository.editNote(id, note)
        }
    }

    fun deleteAllNotes(){
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
        }
    }

    fun deleteNoteById(id: Int){
        viewModelScope.launch {
            noteRepository.deleteNoteById(id)
        }
    }

}