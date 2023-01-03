package io.notly.android.features.note.domain.repository

import io.notly.android.core.NetworkResult
import io.notly.android.features.note.domain.model.Note
import io.notly.android.features.note.domain.model.NoteResponse
import io.notly.android.features.note.domain.model.NotesListResponse

interface NoteRepository {

    suspend fun fetchNotesRemote() : NetworkResult<NotesListResponse>
    suspend fun updateNoteRemote(id: String, note: Note) : NetworkResult<NoteResponse>
    suspend fun createNoteRemote(note: Note) : NetworkResult<NoteResponse>
    suspend fun deleteNoteByIdRemote(id: String) : NetworkResult<NoteResponse>
}