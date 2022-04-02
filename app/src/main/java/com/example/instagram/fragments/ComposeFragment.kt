package com.example.instagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.instagram.LoginActivity
import com.example.instagram.MainActivity
import com.example.instagram.Post
import com.example.instagram.R
import com.google.android.material.button.MaterialButton
import com.parse.ParseFile
import com.parse.ParseUser
import java.io.File


class ComposeFragment : Fragment() {

    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034

    lateinit var ivPreview: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set onClickListeners and setup logic
        ivPreview = view.findViewById(R.id.iv_image)

        view.findViewById<MaterialButton>(R.id.btn_post).setOnClickListener {
            // send post to server
            // get the description that they have entered
            val description = view.findViewById<EditText>(R.id.et_description).text.toString()
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                submitPost(description, user, photoFile!!)
                view.findViewById<EditText>(R.id.et_description).text.clear()
                view.findViewById<ImageView>(R.id.iv_image).setImageResource(0);
                Toast.makeText(requireContext(), "Successfully Posted", Toast.LENGTH_SHORT).show()
            } else {
                Log.e(MainActivity.TAG, "error in sending message")
            }

        }

        view.findViewById<MaterialButton>(R.id.btn_takePicture).setOnClickListener {
            // Launch camera to let user take picture
            onLaunchCamera()
        }

        view.findViewById<Button>(R.id.btn_logout).setOnClickListener{
            ParseUser.logOut()
            Toast.makeText(requireContext(), "Logged out", Toast.LENGTH_SHORT).show()
            goToLoginActivity()
        }



    }

    fun goToLoginActivity() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        // closes out the login activity. Back button exits the app.
        requireActivity().finish()
    }


    // Send a Post object to our Parse server
    fun submitPost(description: String, user: ParseUser, file: File) {
        // create Post object
        val post = Post()
        post.setDescription(description)
        post.setUser(user)
        post.setImage(ParseFile(file))
        post.saveInBackground { exception ->
            if (exception != null) {
                Log.e(MainActivity.TAG, "Error while saving post")
                exception.printStackTrace()
                Toast.makeText(requireContext(), "Posting error occurred", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(MainActivity.TAG, "Post Success")

                // TODO: Reset the EditText field to be empty
                // TODO: Reset the ImageView to post
            }
        }
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(requireContext(), "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }


    // Returns the File for a photo stored on disk given the fileName
    private fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MainActivity.TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(MainActivity.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview

                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }


}