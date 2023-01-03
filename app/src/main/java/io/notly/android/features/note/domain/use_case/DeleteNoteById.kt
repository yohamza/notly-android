package io.notly.android.features.note.domain.use_case

import io.notly.android.core.NetworkResult
import io.notly.android.features.note.domain.repository.NoteRepository
import io.notly.android.features.note.domain.model.NoteResponse

class DeleteNoteById(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(noteId: String) : NetworkResult<NoteResponse> {
        return noteRepository.deleteNoteByIdRemote(noteId)
    }
}