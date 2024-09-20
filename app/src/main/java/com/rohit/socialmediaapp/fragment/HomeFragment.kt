package com.rohit.socialmediaapp.fragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rohit.socialmediaapp.R
import com.rohit.socialmediaapp.databinding.FragmentHomeBinding
import com.rohit.socialmediaapp.model.Post
import com.rohit.socialmediaapp.adapters.PostAdapter
import com.rohit.socialmediaapp.utils.POST
import com.rohit.socialmediaapp.utils.USER_NODE
import com.squareup.picasso.Picasso

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PostAdapter
    private var postListAll = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        adapter = PostAdapter(requireContext(), postListAll)
        binding.postRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.postRecyclerView.adapter = adapter

        loadProfile()
        fetchAllPosts()

        return binding.root
    }


    private fun loadProfile() {
        // Reference to Firestore
        val firestore = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid // Get the current user's ID

        if (userId != null) {
            // Reference to the USER node in Firestore
            val userRef = firestore.collection(USER_NODE).document(userId)

            userRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    // Retrieve the profile image URL from the document
                    val profileImageUrl = document.getString("image") // Adjust the field name as necessary

                    // Load the image using Picasso
                    if (!profileImageUrl.isNullOrEmpty()) {
                        Picasso.get().load(profileImageUrl).into(binding.profileImage)
                    } else {
                        // Set a default image if the URL is empty
                        binding.profileImage.setImageResource(R.drawable.profile) // Replace with your default image
                    }
                } else {
                    // Handle the case where the document does not exist
                    binding.profileImage.setImageResource(R.drawable.profile) // Default image
                }
            }.addOnFailureListener { exception ->
                // Handle any errors
                Log.w(TAG, "Error getting document: ", exception)
                binding.profileImage.setImageResource(R.drawable.profile) // Default image
            }
        } else {
            // Handle case when user ID is null
            binding.profileImage.setImageResource(R.drawable.profile) // Default image
        }
    }


    private fun fetchAllPosts() {
        // Get shorts collection from Firestore

        Firebase.firestore.collection(POST).get()
            .addOnSuccessListener { querySnapshot ->
                postListAll.clear() // Clear the existing list

                for (document in querySnapshot.documents) {
                    val post = document.toObject(Post::class.java)
                    if (post != null) {
                        postListAll.add(post)
                    }
                }

                // Reverse the order of the list, if needed
                postListAll.reverse()

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
            }
    }
}


