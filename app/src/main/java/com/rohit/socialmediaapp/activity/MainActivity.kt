package com.rohit.socialmediaapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rohit.socialmediaapp.R
import com.rohit.socialmediaapp.databinding.ActivityMainBinding
import com.rohit.socialmediaapp.fragment.ActivityFragment
import com.rohit.socialmediaapp.fragment.ExploreFragment
import com.rohit.socialmediaapp.fragment.HomeFragment
import com.rohit.socialmediaapp.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.nav_explore -> {
                    replaceFragment(ExploreFragment())
                    true
                }
                R.id.nav_post -> {
                    val activityFragment = ActivityFragment()
                    activityFragment.show(supportFragmentManager, "ActivityFragment")
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}
