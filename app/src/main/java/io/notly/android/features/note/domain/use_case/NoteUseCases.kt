package io.notly.android.features.note.domain.use_case

data class NoteUseCases(
    val getNotes: GetNotes,
    val updateNote: UpdateNote,
    val createNote: CreateNote,
    val deleteNoteById: DeleteNoteById
)