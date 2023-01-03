package io.notly.android.features.note.domain.model

import io.notly.android.features.note.domain.model.NoteColors.*

data class Note(
    val __v: Int? = null,
    val _id: String? = null,
    val createdAt: String? = null,
    val title: String,
    val description: String,
    val updatedAt: String? = null,
    val userId: String? = null
) {
//    constructor(title: String, description: String) : this(
//        -1,
//        "-1",
//        "12345",
//        title,
//        description,
//        "12345",
//        "123"
//    )

    companion object {
        val noteColors =
            listOf(DefaultColor, BlueColor, RedOrange, Violet, BabyBlue, RedPink, BrownColor)
    }
}

enum class NoteColors(val hex: String) {

    RedOrange("#FF9E80"),
    Violet("#EA80FC"),
    BabyBlue("#B388FF"),
    RedPink("#FF80AB"),
    BlueColor("#366DF8"),
    DefaultColor("#181A21"),
    BrownColor("#252525")


}