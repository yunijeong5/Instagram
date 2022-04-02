package com.example.instagram

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.instagram.fragments.ComposeFragment
import com.example.instagram.fragments.HomeFragment
import com.example.instagram.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.parse.*
import java.io.File

/**
 * Let user create a post by taking a photo with their camera.
 */

class MainActivity : AppCompatActivity() {

    lateinit var bottomNavBar: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.nav_logo_whiteout)
        supportActionBar?.elevation = 0.0F

        val fragmentManager: FragmentManager = supportFragmentManager



        bottomNavBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavBar.elevation = 0.0F

        bottomNavBar.itemRippleColor = ColorStateList.valueOf(
            Color.parseColor("#FFFFFF")
        )

        bottomNavBar.setOnItemSelectedListener { item ->

            var fragmentToShow: Fragment? = null
            when (item.itemId) {
                R.id.action_home -> {
                    fragmentToShow = HomeFragment()
                }
                R.id.action_compose -> {
                    fragmentToShow = ComposeFragment()

                }
                R.id.action_profile -> {
                    fragmentToShow = ProfileFragment()
                }
            }
            if (fragmentToShow != null) {
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow)
                    .commit()
            }
            // Returning true says that we've handled this user interaction on the item
            true
        }

        // Set default selection
        bottomNavBar.selectedItemId = R.id.action_home

//        queryPosts()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}






