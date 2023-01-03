package io.notly.android.features.note.data.repository

import io.notly.android.core.NetworkResult
import io.notly.android.features.note.data.data_source.NoteRemoteDataSource
import io.notly.android.features.note.domain.repository.NoteRepository
import io.notly.android.features.note.domain.model.Note
import io.notly.android.features.note.domain.model.NoteResponse
import io.notly.android.features.note.domain.model.NotesListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val remoteDataSource: NoteRemoteDataSource,
    private val coroutineScope: CoroutineScope) : NoteRepository {

    override suspend fun fetchNotesRemote(): NetworkResult<NotesListResponse> {
        return withContext(coroutineScope.coroutineContext) {
            remoteDataSource.getNotes()
        }
    }

    override suspend fun updateNoteRemote(id: String, note: Note): NetworkResult<NoteResponse> {
        return withContext(coroutineScope.coroutineContext) {
            remoteDataSource.updateNote(id, note)
        }
    }

    override suspend fun createNoteRemote(note: Note): NetworkResult<NoteResponse> {
        return withContext(coroutineScope.coroutineContext) {
            remoteDataSource.createNote(note)
        }
    }

    override suspend fun deleteNoteByIdRemote(id: String) : NetworkResult<NoteResponse> {
        return withContext(coroutineScope.coroutineContext) {
            remoteDataSource.deleteNoteById(id)
        }
    }

}