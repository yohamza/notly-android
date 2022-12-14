package io.notly.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.notly.android.databinding.FragmentAddEditNoteBinding
import io.notly.android.models.Note
import io.notly.android.utils.Constants
import io.notly.android.utils.hide
import io.notly.android.utils.show

@AndroidEntryPoint
class AddEditNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddEditNoteBinding
    private val authViewModel: AuthViewModel by viewModels()
    private var note: Note? = null

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

        binding.deleteNoteBtn.setOnClickListener {
            //TODO: Implement Deletion
        }

        binding.saveBtn.setOnClickListener {
            //TODO: Implement Add and Edit
        }

    }

    private fun initialize() {
        val args = arguments?.getString(Constants.NOTE)

        args?.also {

            note = Gson().fromJson(it, Note::class.java)
            binding.heading.text = "Edit Note"
            binding.deleteNoteBtn.show()
            binding.saveBtn.text = "Update"
            binding.titleEt.setText(note!!.title)
            binding.descriptionEt.setText(note!!.description)

        } ?: kotlin.run {

            binding.heading.text = "Add Note"
            binding.deleteNoteBtn.hide()
            binding.saveBtn.text = "Create"

        }
    }
}