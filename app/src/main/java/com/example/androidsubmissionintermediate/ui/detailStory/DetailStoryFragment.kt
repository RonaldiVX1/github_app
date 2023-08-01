package com.example.androidsubmissionintermediate.ui.detailStory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.androidsubmissionintermediate.R
import com.example.androidsubmissionintermediate.databinding.FragmentDetailStoryBinding


class DetailStoryFragment : Fragment() {

    private var _binding: FragmentDetailStoryBinding? = null
    private val binding get() = _binding
    var description: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailStoryBinding.inflate(layoutInflater)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val detailName = binding?.detailName
        val detailDescription = binding?.detailDescription
        val detailImage = binding?.detailImage

        detailName?.text = arguments?.getString(EXTRA_NAME)
        detailDescription?.text = arguments?.getString(EXTRA_DESCRIPTION)
        Glide.with(requireActivity()).load(arguments?.getString(EXTRA_IMAGE)).into(detailImage!!)

    }
    companion object {
        var EXTRA_NAME = "extra_name"
        var EXTRA_DESCRIPTION = "extra_description"
        var EXTRA_IMAGE = "extra_image"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}