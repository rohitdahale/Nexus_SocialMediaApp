package com.rohit.socialmediaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.rohit.socialmediaapp.R
import com.rohit.socialmediaapp.adapters.ViewPagerAdapter
import com.rohit.socialmediaapp.databinding.FragmentProfileBinding
import com.rohit.socialmediaapp.model.User
import com.rohit.socialmediaapp.utils.USER_NODE
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Initialize ViewPagerAdapter with FragmentManager
        viewPagerAdapter = ViewPagerAdapter(requireActivity().supportFragmentManager)
        viewPagerAdapter.addFragment(MyPostFragment(), "My Post")
        viewPagerAdapter.addFragment(MyShortsFragment(), "My Shorts")

        // Set up ViewPager and TabLayout
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        // Optional: Add page transition animation for smoother effect
        binding.viewPager.setPageTransformer(true) { page, position ->
            page.alpha = 0f
            page.visibility = View.VISIBLE
            // Start Animation
            page.animate()
                .alpha(1f)
                .setDuration(page.resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
                .start()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val user : User = it.toObject<User>()!!
                binding.profileName.text = user.name
                binding.profileEmail.text = user.email
                if (!user.image.isNullOrEmpty()){
                    Picasso.get().load(user.image).into(binding.profileImage)
                }
            }
    }
}