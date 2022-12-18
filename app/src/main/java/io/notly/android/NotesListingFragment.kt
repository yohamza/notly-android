package io.notly.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.setFragmentResultListener
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
import io.notly.android.utils.Constants.TAG
import io.notly.android.utils.NetworkResult.*

@AndroidEntryPoint
class NotesListingFragment : Fragment(), NotesAdapter.NoteClickListener {

    private lateinit var binding: FragmentNotesListingBinding
    private val notesViewModel: NoteViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter
    private var notesList = mutableListOf<Note>()
    private var selectedNotePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setFragmentResultListener("note_added") { requestKey, bundle ->
//            val note: Note = Gson().fromJson(bundle.getString(Constants.NOTE), Note::class.java)
//            notesList.add(note)
//            notesAdapter.notifyItemInserted(notesList.size-1)
//        }
    }

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

    @SuppressLint("NotifyDataSetChanged")
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

//    private fun runGridLayoutAnimation() = binding.notesRv.apply {
//        layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.grid_layout_animation)
//        notesAdapter.notifyDataSetChanged()
//        scheduleLayoutAnimation()
//    }

    override fun onNoteClicked(position: Int) {
        val bundle = Bundle()
        selectedNotePosition = position
        bundle.putString(Constants.NOTE, Gson().toJson(notesList[position]))
        findNavController().navigate(R.id.action_notesListingFragment_to_addEditNoteFragment, bundle)
    }
}