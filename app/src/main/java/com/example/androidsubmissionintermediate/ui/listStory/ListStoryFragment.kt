package com.example.androidsubmissionintermediate.ui.listStory

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidsubmissionintermediate.R
import com.example.androidsubmissionintermediate.data.response.story.Story
import com.example.androidsubmissionintermediate.databinding.FragmentListStoryBinding
import com.example.androidsubmissionintermediate.ui.detailStory.DetailStoryFragment


class ListStoryFragment : Fragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding

    private lateinit var adapter: ListStoriesAdapter

    private val viewModel: ListStoriesViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListStoryBinding.inflate(layoutInflater)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.story.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                adapter.submitData(viewLifecycleOwner.lifecycle, data)
                Log.d("test data", data.toString())
            }
        }
        setupAdapter()


        binding?.buttonCreateStory?.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_list_story_to_createStoryFragment))
        onBackPressed()
    }

    private fun setupAdapter() {
        adapter = ListStoriesAdapter()
        binding?.rvStories?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rvStories?.adapter = adapter


        adapter.setOnItemClickCallback(object : ListStoriesAdapter.OnItemClickCallback {

            val detailStoryFragment = DetailStoryFragment()

            override fun onItemClick(data: Story) {

                val bundle = Bundle()
                bundle.putString(DetailStoryFragment.EXTRA_NAME, data.name)
                bundle.putString(DetailStoryFragment.EXTRA_DESCRIPTION, data.description)
                bundle.putString(DetailStoryFragment.EXTRA_IMAGE, data.photoUrl)
                detailStoryFragment.arguments = bundle
                detailStoryFragment.description
                parentFragmentManager.beginTransaction().apply {
                    replace(
                        R.id.main_fragment,
                        detailStoryFragment,
                        DetailStoryFragment::class.java.simpleName
                    )
                    addToBackStack(null)
                    commit()
                }
            }
        }
        )
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
    }
}