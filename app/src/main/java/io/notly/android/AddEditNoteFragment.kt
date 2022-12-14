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
            //TODO: Implement Deletion
        }

        binding.saveBtn.setOnClickListener {
//            val navController = findNavController()
//            val key = if(isAdd) "note_added" else "note_updated"
//            navController.previousBackStackEntry?.savedStateHandle?.set(key, Gson().toJson(note))
//            navController.popBackStack()
        }

    }

    private fun bindObservers() {

        noteViewModel.statusLiveData.observe(viewLifecycleOwner) {
            binding.progress.hide()
            when (it) {
                is Success -> {
//                    val navController = findNavController()
//                    navController.previousBackStackEntry?.savedStateHandle?.set("note_deleted", "Note ${note!!._id} deleted successfully")
//                    navController.popBackStack()
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
//                    val navController = findNavController()
//                    val key = if(isAdd) "note_added" else "note_updated"
//                    navController.previousBackStackEntry?.savedStateHandle?.set(key, note)
//                    navController.popBackStack()
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