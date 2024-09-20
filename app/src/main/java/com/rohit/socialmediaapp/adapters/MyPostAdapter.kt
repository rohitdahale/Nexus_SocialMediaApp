package com.rohit.socialmediaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.socialmediaapp.databinding.MyPostRvBinding
import com.rohit.socialmediaapp.model.PostModel
import com.squareup.picasso.Picasso

class MyPostAdapter(var context: Context, var postList: ArrayList<PostModel>) :RecyclerView.Adapter<MyPostAdapter.ViewHolder>(){

    inner class ViewHolder(var binding: MyPostRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MyPostRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(postList.get(position).postUrl).into(holder.binding.postImageProfile)
    }
}