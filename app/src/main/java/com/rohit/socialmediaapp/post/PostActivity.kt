package com.rohit.socialmediaapp.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rohit.socialmediaapp.R
import com.rohit.socialmediaapp.databinding.ActivityPostBinding
import com.rohit.socialmediaapp.model.PostModel
import com.rohit.socialmediaapp.utils.POST
import com.rohit.socialmediaapp.utils.POST_FOLDER
import com.rohit.socialmediaapp.utils.USER_NODE
import com.rohit.socialmediaapp.utils.uploadImage

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    var imageUrl: String? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) { url ->
                if (url != null) {
                    binding.postImageView.setImageURI(uri)
                    imageUrl = url
                } else {
                    Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.postImageView.setOnClickListener {
            launcher.launch("image/*")
        }

        binding.postFab.setOnClickListener {
            val caption = binding.captionEditText.text.toString()
            val location = binding.locationEditText.text.toString()

            if (imageUrl == null || caption.isBlank()) {
                Toast.makeText(this, "Please provide an image and caption", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val postId = Firebase.firestore.collection(POST).document().id // Create unique post ID
            val post = PostModel(postId, imageUrl!!, caption, location, Firebase.auth.currentUser!!.uid)

            // Save post to the global POST collection
            Firebase.firestore.collection(POST).document(postId).set(post)
                .addOnSuccessListener {
                    // Save post to the current user's node
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid)
                        .collection(POST)
                        .document(postId).set(post)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Post uploaded successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to upload post in user node", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload post", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
