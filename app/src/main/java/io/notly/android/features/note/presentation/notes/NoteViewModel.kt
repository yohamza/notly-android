package io.notly.android.features.note.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.notly.android.core.NetworkResult.*
import io.notly.android.features.note.domain.use_case.NoteUseCases
import io.notly.android.features.note.presentation.ui_state.NoteUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val notesUseCases: NoteUseCases): ViewModel() {

    private var _uiState = MutableStateFlow(NoteUiState(isLoading = true))
    val uiState get() = _uiState.asStateFlow()

    private var fetchNotes: Job? = null

    fun getNotes(){

        fetchNotes?.cancel()

        fetchNotes = viewModelScope.launch {
            val result = notesUseCases.getNotes()

            _uiState.update { it.copy(isLoading = true) }

            when(result) {
                is Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "",
                            notesList = result.data?.notesList
                        )
                    }
                }
                is Loading -> {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            errorMessage = ""
                        )
                    }
                }
                is Error -> {
                    _uiState.update {
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