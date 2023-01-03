package io.notly.android.features.note.presentation.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.notly.android.R
import io.notly.android.databinding.FragmentNotesListingBinding
import io.notly.android.features.note.domain.model.Note
import io.notly.android.utils.*
import io.notly.android.features.note.presentation.ui_state.NoteUiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesListingFragment : Fragment(), NotesAdapter.NoteClickListener {

    private lateinit var binding: FragmentNotesListingBinding
    private val notesViewModel: NoteViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter
    private var notesList = mutableListOf<Note>()
    private var selectedNotePosition = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesListingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeToRefresh.setOnRefreshListener {
            notesViewModel.getNotes()
        }

        binding.createBtn.setOnClickListener {
            findNavController().navigate(R.id.action_notesListingFragment_to_addEditNoteFragment)
        }

        getNotes()
        bindRecyclerViewAdapter()

    }

    private fun getNotes(){
        if (requireActivity().checkConnection()) {
            binding.swipeToRefresh.isRefreshing = true
            subscribeQuery()
        } else {
            binding.createBtn.snack(getString(R.string.no_internet))
        }
    }

    private fun subscribeQuery() {

        this.lifecycleScope.launch {
            requireActivity().repeatOnLifecycle(Lifecycle.State.CREATED) {
                notesViewModel.apply {
                    getNotes()
                    uiState.collect {
                        handleUiState(it)
                    }
                }
            }
        }

    }

    private fun handleUiState(UiState: NoteUiState) {
        UiState.apply {
            binding.swipeToRefresh.isRefreshing = this.isLoading

            if(!this.isLoading){
                if(this.errorMessage.isEmpty()){
                    populateBinding(notesListParam = notesList)
                }
                else{
                    binding.noDataHolder.show()
                    binding.animationText.text = this.errorMessage
                }
            }
        }
    }

    private fun populateBinding(notesListParam: List<Note>?){

        if(notesListParam.isNullOrEmpty()){
            binding.noDataHolder.show()
        }
        else{
            binding.noDataHolder.hide()
            notesList.clear()
            notesList.addAll(notesListParam.toMutableList())
            notesAdapter.notifyDataSetChanged()
        }

    }

    private fun bindRecyclerViewAdapter() {
        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        notesAdapter = NotesAdapter(notesList, ::onNoteClicked)
        binding.notesRv.layoutManager = layoutManager
        binding.notesRv.adapter = notesAdapter
    }

    override fun onNoteClicked(position: Int) {
        val bundle = Bundle()
        selectedNotePosition = position
        bundle.putString(Constants.NOTE, Gson().toJson(notesList[position]))
        findNavController().navigate(R.id.action_notesListingFragment_to_addEditNoteFragment, bundle)
    }
}