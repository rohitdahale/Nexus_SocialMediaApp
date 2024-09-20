package com.rohit.socialmediaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rohit.socialmediaapp.databinding.MyShortsRvBinding
import com.rohit.socialmediaapp.model.ShortModel

class MyShortsAdapter(var context: Context, var shortList: ArrayList<ShortModel>) : RecyclerView.Adapter<MyShortsAdapter.ViewHolder>(){

    inner class ViewHolder(var binding: MyShortsRvBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = MyShortsRvBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = shortList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(shortList.get(position).postUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.shortsThumbnail)
    }
}