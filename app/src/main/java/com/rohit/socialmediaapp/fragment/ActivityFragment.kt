package com.rohit.socialmediaapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rohit.socialmediaapp.databinding.FragmentActivityBinding
import com.rohit.socialmediaapp.post.PostActivity
import com.rohit.socialmediaapp.post.ShortsActivity

class ActivityFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentActivityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentActivityBinding.inflate(inflater, container, false)

        binding.createPostCard.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),PostActivity::class.java))
        }
        binding.createShortsCard.setOnClickListener {
            activity?.startActivity(Intent(requireContext(),ShortsActivity::class.java))
        }
        return binding.root
    }
}
