package io.notly.android.features.note.domain.use_case

import io.notly.android.core.NetworkResult
import io.notly.android.features.note.domain.repository.NoteRepository
import io.notly.android.features.note.domain.model.NotesListResponse

class GetNotes(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(): NetworkResult<NotesListResponse> {
        return noteRepository.fetchNotesRemote()
    }
}