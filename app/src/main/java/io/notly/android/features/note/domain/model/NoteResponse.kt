package io.notly.android.features.note.domain.model

import com.google.gson.annotations.SerializedName

data class NoteResponse(
    val message: String? = null,
    @SerializedName("data")
    val note: Note? = null
)