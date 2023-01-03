package io.notly.android.features.note.presentation.ui_state

import io.notly.android.features.note.domain.model.Note

data class NoteUiState (
    val isLoading: Boolean = true,
    val errorMessage: String = "",
    val note: Note? = null,
    val notesList: List<Note>? = null
)