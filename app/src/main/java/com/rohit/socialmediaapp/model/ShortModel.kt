package com.rohit.socialmediaapp.model

data class ShortModel(
    var shortId: String = "",       // Unique ID for the short video
    var postUrl: String = "",      // URL of the short video
    var caption: String = "",       // Caption for the short
    var duration: Int = 0,          // Duration of the video in seconds (optional)
    var userId: String = "",
    var profileLink:String = ""// ID of the user who created the short
    // Timestamp for when the short was posted (optional)
)

