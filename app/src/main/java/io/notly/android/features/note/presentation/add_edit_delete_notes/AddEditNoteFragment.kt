package io.notly.android.features.note.presentation.add_edit_delete_notes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
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
    private var didChange = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeScreenBehavior()
        initListeners()

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    //Default behaviour of onBackPressed
                    isEnabled = false
                    activity?.onBackPressedDispatcher?.onBackPressed()

                    //binding.saveBtn.performClick()

                }

            })

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

    private fun initListeners() {

        binding.backBtn.setOnClickListener {
            if (didChange)
                binding.saveBtn.performClick()
            else
                findNavController().popBackStack()
        }

        binding.deleteNoteBtn.setOnClickListener {
            note?.let {
                it._id?.let { id ->
                    deleteNote(id)
                }
            }
        }

        binding.titleEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.saveBtn.show()
                if (!didChange) didChange = true
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.descriptionEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.saveBtn.show()
                if (!didChange) didChange = true
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.saveBtn.setOnClickListener {
            val title = binding.titleEt.text.toString().trim()

            if(title.isEmpty()) {
                it.hideKeyboard()
                it.snack("Title can't be empty")
                return@setOnClickListener
            }

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

    private fun initializeScreenBehavior() {
        val args = arguments?.getString(Constants.NOTE)

        args?.also {
            isAdd = false
            note = Gson().fromJson(it, Note::class.java)
            binding.deleteNoteBtn.show()
            binding.saveBtn.text = "Update"
            if(!note!!.title.isNullOrEmpty()) binding.titleEt.setText(note!!.title)
            if(!note!!.description.isNullOrEmpty()) binding.descriptionEt.setText(note!!.description)

        } ?: kotlin.run {
            isAdd = true
            binding.deleteNoteBtn.hide()
            binding.saveBtn.text = "Create"

        }
    }
}