package io.notly.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.notly.android.databinding.FragmentAddEditNoteBinding
import io.notly.android.models.Note
import io.notly.android.models.NoteRequest
import io.notly.android.utils.*
import io.notly.android.utils.NetworkResult.*

@AndroidEntryPoint
class AddEditNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddEditNoteBinding
    private val noteViewModel: NoteViewModel by viewModels()
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
        bindObservers()
        
        binding.heading.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.deleteNoteBtn.setOnClickListener {
            note?.let {
                noteViewModel.deleteNoteById(it._id)
            }
        }

        binding.saveBtn.setOnClickListener {
            val title = binding.titleEt.text.toString().trim()
            val description = binding.descriptionEt.text.toString().trim()
            val noteRequest = NoteRequest(title, description)
            if(isAdd){
                noteViewModel.createNote(noteRequest)
            }
            else{
                noteViewModel.updateNote(note!!._id, noteRequest)
            }
        }

    }

    private fun bindObservers() {

        noteViewModel.statusLiveData.observe(viewLifecycleOwner) {
            binding.progress.hide()
            when (it) {
                is Success -> {
                    //TODO: Implement persistence logic for delete
                    findNavController().popBackStack()
                }
                is Loading -> {
                    binding.progress.show()
                }
                is Error -> {
                    binding.heading.snack(it.message)
                }
            }
        }

        noteViewModel.noteLiveData.observe(viewLifecycleOwner) {
            binding.progress.hide()
            when (it) {
                is Success -> {
                    //TODO: Implement persistence logic for add/update
                    findNavController().popBackStack()
                }
                is Loading -> {
                    binding.progress.show()
                }
                is Error -> {
                    binding.heading.snack(it.message)
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