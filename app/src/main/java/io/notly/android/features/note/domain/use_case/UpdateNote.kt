package io.notly.android.features.note.domain.use_case

import io.notly.android.core.NetworkResult
import io.notly.android.features.note.domain.model.Note
import io.notly.android.features.note.domain.repository.NoteRepository
import io.notly.android.features.note.domain.model.NoteResponse

class UpdateNote(private val noteRepository: NoteRepository) {

    suspend operator fun invoke(noteId: String, note: Note) : NetworkResult<NoteResponse> {
        return noteRepository.updateNoteRemote(noteId, note)
    }

}