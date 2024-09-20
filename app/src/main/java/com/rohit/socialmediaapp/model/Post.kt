package com.rohit.socialmediaapp.model

data class Post(
    var postId: String = "",       // Unique post ID
    var postUrl: String = "",      // URL of the post image
    var caption: String = "",
    val userId : String = "",
    var location: String = ""
)
