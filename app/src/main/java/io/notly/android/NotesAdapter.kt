package io.notly.android

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.notly.android.databinding.NoteItemBinding
import io.notly.android.models.Note
import io.notly.android.models.NoteResponse

class NotesAdapter(private val notesList: List<Note>, private val onNoteClicked: (Int) -> Unit) :
    RecyclerView.Adapter<NotesAdapter.NoteVH>() {

    private lateinit var context: Context
    private lateinit var binding: NoteItemBinding
    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteVH {
        context = parent.context
        inflater = LayoutInflater.from(context)
        binding = NoteItemBinding.inflate(inflater, parent, false)

        return NoteVH(binding)
    }

    override fun onBindViewHolder(holder: NoteVH, position: Int) {
        val note: Note = notesList[position]
        holder.bind(note, position)
    }

    override fun getItemCount(): Int = notesList.size

    interface NoteClickListener{
        fun onNoteClicked(position: Int)
    }

    inner class NoteVH(private val noteItemBinding: NoteItemBinding) :
        RecyclerView.ViewHolder(noteItemBinding.root) {

        fun bind(note: Note, position: Int) {
            noteItemBinding.title.text = note.title
            noteItemBinding.description.text = note.description
            noteItemBinding.root.setOnClickListener {
                onNoteClicked(position)
            }
        }
    }
}