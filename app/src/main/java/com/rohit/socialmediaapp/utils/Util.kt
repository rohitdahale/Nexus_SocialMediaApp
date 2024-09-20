package com.rohit.socialmediaapp.utils

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri: Uri, folderName: String, callback: (String) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().getReference(folderName)
        .child(UUID.randomUUID().toString())

    storageRef.putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { downloadUri ->
                callback(downloadUri.toString())
            }
        }
        .addOnFailureListener {
            callback("")
        }
}
