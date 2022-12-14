package io.notly.android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.notly.android.databinding.FragmentNotesListingBinding
import io.notly.android.models.Note
import io.notly.android.models.NoteResponse
import io.notly.android.utils.*
import io.notly.android.utils.NetworkResult.*

@AndroidEntryPoint
class NotesListingFragment : Fragment(), NotesAdapter.NoteClickListener {

    private lateinit var binding: FragmentNotesListingBinding
    private val notesViewModel: NoteViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter
    private var notesList = mutableListOf<Note>()

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

        val layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        notesAdapter = NotesAdapter(notesList, ::onNoteClicked)
        binding.notesRv.layoutManager = layoutManager
        binding.notesRv.adapter = notesAdapter

        bindObservers()
        notesViewModel.getNotes()
    }

    private fun bindObservers() {
        notesViewModel.notesListLiveData.observe(viewLifecycleOwner) {
            binding.swipeToRefresh.isRefreshing = false
            when (it) {
                is Success -> {
                    notesList.clear()
                    notesList.addAll(it.data?.notesList!!.toMutableList())
                    if(notesList.isNotEmpty()){
                        notesAdapter.notifyDataSetChanged()
                    }
                    else{
                        binding.noDataHolder.show()
                    }
                }
                is Loading -> {
                    binding.swipeToRefresh.isRefreshing = true
                    binding.noDataHolder.hide()
                }
                is Error -> {
                    binding.title.snack(it.message)
                    binding.animationText.text = it.message
                    binding.noDataHolder.show()
                }
            }

        }
    }

    override fun onNoteClicked(note: Note) {
        val bundle = Bundle()
        bundle.putString(Constants.NOTE, Gson().toJson(note))
        findNavController().navigate(R.id.action_notesListingFragment_to_addEditNoteFragment, bundle)
    }
}