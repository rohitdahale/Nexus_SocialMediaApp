package com.rohit.socialmediaapp.model

data class PostModel(
    var postId: String = "",       // Unique post ID
    var postUrl: String = "",      // URL of the post image
    var caption: String = "",      // Post caption
    var location: String = "",     // Location (optional)
    var userId: String = ""        // ID of the user who created the post
)
