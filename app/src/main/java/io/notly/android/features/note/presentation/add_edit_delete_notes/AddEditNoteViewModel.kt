package io.notly.android.features.note.presentation.add_edit_delete_notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.notly.android.core.NetworkResult.*
import io.notly.android.features.note.domain.model.Note
import io.notly.android.features.note.domain.use_case.NoteUseCases
import io.notly.android.features.note.presentation.ui_state.NoteUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(private val noteUseCases: NoteUseCases) :
    ViewModel() {

    private var _createNoteUiState = MutableStateFlow(NoteUiState(isLoading = true))
    val createNoteUiState get() = _createNoteUiState

    private var _updateNoteUiState = MutableStateFlow(NoteUiState(isLoading = true))
    val updateNoteUiState get() = _updateNoteUiState

    private var _deleteNoteUiState = MutableStateFlow(NoteUiState(isLoading = true))
    val deleteNoteUiState get() = _deleteNoteUiState

    private var createNoteJob: Job? = null
    private var updateNoteJob: Job? = null
    private var deleteNoteJob: Job? = null

    fun createNote(note: Note) {

        createNoteJob?.cancel()
        createNoteJob = viewModelScope.launch {

            val result = noteUseCases.createNote(note)
            _createNoteUiState.update { it.copy(isLoading = true) }

            when (result) {
                is Success -> {
                    _createNoteUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "",
                            note = result.data?.note
                        )
                    }
                }
                is Loading -> {
                    _createNoteUiState.update {
                        it.copy(
                            isLoading = true,
                            errorMessage = ""
                        )
                    }
                }
                is Error -> {
                    _createNoteUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Something went wrong"
                        )
                    }
                }
            }

        }
    }

    fun updateNote(id: String, note: Note) {

        updateNoteJob?.cancel()

        updateNoteJob = viewModelScope.launch {
            val result = noteUseCases.updateNote(id, note)

            _updateNoteUiState.update { it.copy(isLoading = true) }
            when (result) {
                is Success -> {
                    _updateNoteUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "",
                            note = result.data?.note
                        )
                    }
                }
                is Loading -> {
                    _updateNoteUiState.update {
                        it.copy(
                            isLoading = true,
                            errorMessage = ""
                        )
                    }
                }
                is Error -> {
                    _updateNoteUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Something went wrong"
                        )
                    }
                }
            }
        }
    }

    fun deleteNoteById(id: String) {

        deleteNoteJob?.cancel()

        deleteNoteJob = viewModelScope.launch {
            val result = noteUseCases.deleteNoteById(id)

            _deleteNoteUiState.update { it.copy(isLoading = true) }
            when (result) {
                is Success -> {
                    _deleteNoteUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = ""
                        )
                    }
                }
                is Loading -> {
                    _deleteNoteUiState.update {
                        it.copy(
                            isLoading = true,
                            errorMessage = ""
                        )
                    }
                }
                is Error -> {
                    _deleteNoteUiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Something went wrong"
                        )
                    }
                }
            }
        }
    }

}