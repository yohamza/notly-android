package io.notly.android.features.note.domain.model

import com.google.gson.annotations.SerializedName

data class NotesListResponse(
    val message: String? = null,
    @SerializedName("data")
    val notesList: List<Note>? = null
)