package io.notly.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.notly.android.databinding.FragmentNotesListingBinding
import io.notly.android.utils.NetworkResult
import io.notly.android.utils.NetworkResult.*
import io.notly.android.utils.show

@AndroidEntryPoint
class NotesListingFragment : Fragment() {

    private lateinit var binding: FragmentNotesListingBinding
    private val notesViewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesListingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesViewModel.notesListLiveData.observe(viewLifecycleOwner) {

            when(it){
                is Success -> {
                    //TODO: Bind items to recyclerview
                }
                is Loading -> {
                    binding.progress.show()
                }
                is Error -> {
                    //TODO: Show Error message
                }
            }

        }
    }
}