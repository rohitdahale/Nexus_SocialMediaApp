package com.rohit.socialmediaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rohit.socialmediaapp.adapters.MyShortsAdapter
import com.rohit.socialmediaapp.databinding.FragmentMyShortsBinding
import com.rohit.socialmediaapp.model.ShortModel
import com.rohit.socialmediaapp.utils.SHORTS
import com.rohit.socialmediaapp.utils.USER_NODE

class MyShortsFragment : Fragment() {

    private lateinit var binding: FragmentMyShortsBinding
    private lateinit var adapter: MyShortsAdapter
    private var shortList = ArrayList<ShortModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyShortsBinding.inflate(inflater,container,false)

        adapter = MyShortsAdapter(requireContext(), shortList)
        binding.shortsRecyclerView.layoutManager = StaggeredGridLayoutManager(3,
            StaggeredGridLayoutManager.VERTICAL)
        binding.shortsRecyclerView.adapter = adapter

        fetchUserShorts()

        return binding.root

    }

    private fun fetchUserShorts() {

        val userId = Firebase.auth.currentUser!!.uid  // Get current user's ID

        // Query posts specific to the current user
        Firebase.firestore.collection(USER_NODE)
            .document(userId)
            .collection(SHORTS)  // Assuming POSTS collection is inside user's document
            .get()
            .addOnSuccessListener { documents ->
                shortList.clear()  // Clear the list before adding new data
                for (document in documents) {
                    val short = document.toObject(ShortModel::class.java)  // Convert to PostModel
                    shortList.add(short)  // Add each post to the list
                }
                adapter.notifyDataSetChanged()  // Notify adapter of data change
            }
            .addOnFailureListener {
                // Handle any errors here
            }

    }
}