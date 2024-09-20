package com.rohit.socialmediaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rohit.socialmediaapp.adapters.ShortAdapter
import com.rohit.socialmediaapp.databinding.FragmentExploreBinding
import com.rohit.socialmediaapp.model.ShortModel
import com.rohit.socialmediaapp.utils.SHORTS

class ExploreFragment : Fragment() {

    private lateinit var binding: FragmentExploreBinding
    private lateinit var adapter: ShortAdapter
    private var shortList = ArrayList<ShortModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(inflater, container, false)

        // Fetch shorts before initializing adapter
        fetchShorts()

        return binding.root
    }

    private fun fetchShorts() {
        // Get shorts collection from Firestore
        Firebase.firestore.collection(SHORTS).get()
            .addOnSuccessListener { querySnapshot ->
                shortList.clear() // Clear the existing list

                for (document in querySnapshot.documents) {
                    val short = document.toObject(ShortModel::class.java)
                    if (short != null) {
                        shortList.add(short)
                    }
                }

                // Reverse the order of the list, if needed
                shortList.reverse()

                // Set up the adapter and notify it of the data change
                adapter = ShortAdapter(requireContext(), shortList)
                binding.viewPager.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Handle any errors here (e.g., show a toast or log the error)
                // For example:
                // Toast.makeText(requireContext(), "Failed to load shorts", Toast.LENGTH_SHORT).show()
            }
    }
}
