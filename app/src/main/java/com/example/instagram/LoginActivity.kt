package com.example.instagram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.parse.ParseUser

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

//         check if there's a user logged in
//         if there is, tke them to MainActivity
        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity()
        }

        findViewById<MaterialButton>(R.id.btn_login).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            loginUser(username, password)
        }

        findViewById<MaterialButton>(R.id.btn_signup).setOnClickListener{
            val username = findViewById<EditText>(R.id.et_username).text.toString()
            val password = findViewById<EditText>(R.id.et_password).text.toString()
            signUpUser(username, password)
        }
    }

    private fun signUpUser(username: String, password: String) {
        // Create the ParseUser
        val user = ParseUser()

        // Set fields for the user to be created
        user.setUsername(username)
        user.setPassword(password)

        user.signUpInBackground { e ->
            if (e == null) {
                // Hooray! User has successfully created a new account

                // Navigate the user to the MainActivity
                goToMainActivity() // move to SetProfileActivity
                // Show a toast to indicate user successfully signed up for an account
                Toast.makeText(this, "Successfully created account", Toast.LENGTH_SHORT).show()


            } else {
                // Sign up didn't succeed. Look at the ParseException to figure out what went wrong
                // show a toast to tell user sign up was not successful
                e.printStackTrace()
                Toast.makeText(this, "error signing up", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        ParseUser.logInInBackground(username, password, ({ user, e ->
            if (user != null) {
                Log.i(TAG, "Hooray!  The user is logged in.")
                goToMainActivity()
            } else {
                // Signup failed.  Look at the ParseException to see what happened.
                e.printStackTrace()
                Toast.makeText(this, "error logging in", Toast.LENGTH_SHORT).show()
            }})
        )
    }

    private fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
        // closes out the login activity. Back button exits the app.
        finish()
    }

    private fun goToProfileSetup() {
        val intent = Intent(this@LoginActivity, SetProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        const val TAG = "LoginActivity"
    }
}