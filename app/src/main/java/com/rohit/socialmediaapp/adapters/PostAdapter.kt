package com.rohit.socialmediaapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.rohit.socialmediaapp.R
import com.rohit.socialmediaapp.databinding.ItemPostBinding
import com.rohit.socialmediaapp.model.Post
import com.rohit.socialmediaapp.model.User
import com.rohit.socialmediaapp.utils.USER_NODE
import com.squareup.picasso.Picasso

class PostAdapter(val context: Context, val postList: ArrayList<Post>): RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    inner class ViewHolder(var binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val post = postList[position]

        Firebase.firestore.collection(USER_NODE).document(postList.get(position).userId).get().addOnSuccessListener {
            val user = it.toObject<User>()
            Glide.with(context).load(user!!.image).placeholder(R.drawable.profile).into(holder.binding.postUserImageView)
            holder.binding.postUserNameTextView.text = user.name
        }

        holder.binding.postContentTextView.setText(postList.get(position).caption)
        Picasso.get().load(postList.get(position).postUrl).into(holder.binding.postImageView)
        holder.binding.heart.setOnClickListener {
            holder.binding.heart.setImageResource(R.drawable.heart_click)
        }
        holder.binding.share.setOnClickListener {
            sharePost(post)
        }
    }

    private fun sharePost(post: Post) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Check out this post: ${post.caption}\n\nImage: ${post.postUrl}")
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share post via"))
    }
}