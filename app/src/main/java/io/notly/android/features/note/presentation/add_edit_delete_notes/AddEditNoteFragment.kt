package io.notly.android.features.note.presentation.add_edit_delete_notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.notly.android.R
import io.notly.android.databinding.FragmentAddEditNoteBinding
import io.notly.android.features.note.domain.model.Note
import io.notly.android.utils.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddEditNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddEditNoteBinding
    private val noteViewModel: AddEditNoteViewModel by viewModels()
    private var note: Note? = null
    private var isAdd = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialize()

        binding.heading.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.deleteNoteBtn.setOnClickListener {
            note?.let {
                it._id?.let { id ->
                    deleteNote(id)
                }
            }
        }

        binding.saveBtn.setOnClickListener {
            val title = binding.titleEt.text.toString().trim()
            val description = binding.descriptionEt.text.toString().trim()
            val noteReq = Note(title = title, description = description)
            if (isAdd) {
                createNote(noteReq)
            } else {
                note?._id?.let {
                    updateNote(it, noteReq)
                }
            }
        }

    }

    private fun createNote(note: Note) {
        if (requireActivity().checkConnection()) {
            bindCreateNoteObserver(note)
        } else {
            binding.saveBtn.snack(getString(R.string.no_internet))
        }
    }

    private fun updateNote(id: String, note: Note) {
        if (requireActivity().checkConnection()) {
            bindUpdateNoteObserver(id, note)
        } else {
            binding.saveBtn.snack(getString(R.string.no_internet))
        }
    }

    private fun deleteNote(id: String) {
        if (requireActivity().checkConnection()) {
            bindDeleteNoteObserver(id)
        } else {
            binding.saveBtn.snack(getString(R.string.no_internet))
        }
    }

    private fun bindCreateNoteObserver(note: Note) {

        this.lifecycleScope.launch {
            noteViewModel.apply {
                createNote(note)
                createNoteUiState.collect {
                    if (!it.isLoading) {
                        if (it.errorMessage.isEmpty()) {
                            findNavController().popBackStack()
                        } else {
                            binding.saveBtn.snack(it.errorMessage)
                        }
                    }
                }
            }

        }
    }

    private fun bindUpdateNoteObserver(id: String, note: Note) {

        this.lifecycleScope.launch {
            noteViewModel.apply {
                updateNote(id, note)
                updateNoteUiState.collect {
                    if (!it.isLoading) {
                        if (it.errorMessage.isEmpty()) {
                            findNavController().popBackStack()
                        } else {
                            binding.saveBtn.snack(it.errorMessage)
                        }
                    }
                }
            }

        }
    }

    private fun bindDeleteNoteObserver(id: String) {

        this.lifecycleScope.launch {
            noteViewModel.apply {
                deleteNoteById(id)
                deleteNoteUiState.collect {
                    if (!it.isLoading) {
                        if (it.errorMessage.isEmpty()) {
                            findNavController().popBackStack()
                        } else {
                            binding.saveBtn.snack(it.errorMessage)
                        }
                    }
                }
            }

        }
    }

    private fun initialize() {
        val args = arguments?.getString(Constants.NOTE)

        args?.also {
            isAdd = false
            note = Gson().fromJson(it, Note::class.java)
            binding.heading.text = "Edit Note"
            binding.deleteNoteBtn.show()
            binding.saveBtn.text = "Update"
            binding.titleEt.setText(note!!.title)
            binding.descriptionEt.setText(note!!.description)

        } ?: kotlin.run {
            isAdd = true
            binding.heading.text = "Add Note"
            binding.deleteNoteBtn.hide()
            binding.saveBtn.text = "Create"

        }
    }
}