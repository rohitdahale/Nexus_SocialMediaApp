package com.rohit.socialmediaapp.post

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.rohit.socialmediaapp.databinding.ActivityShortsBinding
import com.rohit.socialmediaapp.fragment.HomeFragment
import com.rohit.socialmediaapp.model.PostModel
import com.rohit.socialmediaapp.utils.SHORTS
import com.rohit.socialmediaapp.utils.SHORT_FOLDER
import com.rohit.socialmediaapp.utils.USER_NODE
import java.text.DecimalFormat

class ShortsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShortsBinding
    var videoUrl: String? = null
    var videoUri: Uri? = null

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            videoUri = it
            previewVideo(it)
            binding.progressBar.progress = 0
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.progressPercentage.visibility = android.view.View.VISIBLE
            uploadVideoWithProgress(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set click listener for the upload button
        binding.uploadButton.setOnClickListener {
            launcher.launch("video/*")
        }

        // Set click listener for the post button
        binding.postFab.setOnClickListener {
            val caption = binding.captionEditText.text.toString()

            if (videoUrl == null || caption.isBlank()) {
                Toast.makeText(this, "Please upload a video and provide a caption", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val shortId = Firebase.firestore.collection(SHORTS).document().id // Create unique post ID
            val short = PostModel(shortId, videoUrl!!, caption, Firebase.auth.currentUser!!.uid)

            // Save post to the global SHORTS collection
            Firebase.firestore.collection(SHORTS).document(shortId).set(short)
                .addOnSuccessListener {
                    // Save post to the current user's node
                    Firebase.firestore.collection(USER_NODE)
                        .document(Firebase.auth.currentUser!!.uid)
                        .collection(SHORTS)
                        .document(shortId).set(short)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Short uploaded successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@ShortsActivity, HomeFragment::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to upload short in user node", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to upload short", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Function to preview the selected video in the VideoView
    private fun previewVideo(uri: Uri) {
        val mediaController = MediaController(this)
        binding.videoPreview.setMediaController(mediaController)
        binding.videoPreview.setVideoURI(uri)
        binding.videoPreview.requestFocus()
        binding.videoPreview.start()
    }

    // Function to upload video with progress tracking
    private fun uploadVideoWithProgress(uri: Uri) {
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child(SHORT_FOLDER)
            .child("${System.currentTimeMillis()}.mp4")

        val uploadTask = storageRef.putFile(uri)

        uploadTask.addOnProgressListener { taskSnapshot ->
            val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
            val decimalFormat = DecimalFormat("##")
            val progressPercentage = decimalFormat.format(progress)

            // Update progress bar and text
            binding.progressBar.progress = progress.toInt()
            binding.progressPercentage.text = "$progressPercentage%"

        }.addOnSuccessListener { taskSnapshot ->
            // Get the uploaded video URL
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { url ->
                videoUrl = url.toString()
                binding.progressBar.visibility = android.view.View.GONE
                binding.progressPercentage.visibility = android.view.View.GONE
                Toast.makeText(this, "Video uploaded successfully", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            binding.progressBar.visibility = android.view.View.GONE
            binding.progressPercentage.visibility = android.view.View.GONE
            Toast.makeText(this, "Video upload failed", Toast.LENGTH_SHORT).show()
        }
    }
}
