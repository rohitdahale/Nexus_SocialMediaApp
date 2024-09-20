package com.rohit.socialmediaapp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.ktx.Firebase
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.rohit.socialmediaapp.adapters.MyPostAdapter
import com.rohit.socialmediaapp.databinding.FragmentMyPostBinding
import com.rohit.socialmediaapp.model.PostModel
import com.rohit.socialmediaapp.utils.POST
import com.rohit.socialmediaapp.utils.USER_NODE

class MyPostFragment : Fragment() {

    private lateinit var binding: FragmentMyPostBinding
    private lateinit var adapter: MyPostAdapter
    private var postList = ArrayList<PostModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPostBinding.inflate(layoutInflater,container,false)

        adapter = MyPostAdapter(requireContext(), postList)
        binding.myPostRecycler.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        binding.myPostRecycler.adapter = adapter

        fetchUserPosts()

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchUserPosts() {
        val userId = Firebase.auth.currentUser!!.uid  // Get current user's ID

        // Query posts specific to the current user
        Firebase.firestore.collection(USER_NODE)
            .document(userId)
            .collection(POST)  // Assuming POSTS collection is inside user's document
            .get()
            .addOnSuccessListener { documents ->
                postList.clear()  // Clear the list before adding new data
                for (document in documents) {
                    val post = document.toObject(PostModel::class.java)  // Convert to PostModel
                    postList.add(post)  // Add each post to the list
                }
                adapter.notifyDataSetChanged()  // Notify adapter of data change
            }
            .addOnFailureListener {
                // Handle any errors here
            }
    }

}