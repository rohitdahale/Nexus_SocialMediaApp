package com.rohit.socialmediaapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rohit.socialmediaapp.databinding.ActivitySignUpBinding
import com.rohit.socialmediaapp.model.User
import com.rohit.socialmediaapp.utils.USER_NODE
import com.rohit.socialmediaapp.utils.USER_PROFILE_FOLDER
import com.rohit.socialmediaapp.utils.uploadImage

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private var user: User = User() // Initialized the user

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(it, USER_PROFILE_FOLDER) { imageUrl ->
                user.image = imageUrl
                Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                binding.logoImageView.setImageURI(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                // Assign user details before calling signUp
                user.name = name
                user.email = email
                user.password = password

                signUp(name, email, password)
            }
        }

        binding.logoImageView.setOnClickListener {
            launcher.launch("image/*") // Trigger image picker
        }

        binding.loginPromptTextView.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                // Save user data to Firestore with image URL if available
                Firebase.firestore.collection(USER_NODE)
                    .document(Firebase.auth.currentUser!!.uid)
                    .set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "User saved successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to save user: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, result.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
