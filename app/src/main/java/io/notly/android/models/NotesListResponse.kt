package io.notly.android.models

import com.google.gson.annotations.SerializedName

data class NotesListResponse(
    val message: String? = null,
    @SerializedName("data")
    val notesList: List<Note>? = null
)