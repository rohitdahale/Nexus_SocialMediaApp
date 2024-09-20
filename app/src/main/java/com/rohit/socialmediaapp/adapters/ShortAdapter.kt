package com.rohit.socialmediaapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rohit.socialmediaapp.R
import com.rohit.socialmediaapp.databinding.ShortDesignBinding
import com.rohit.socialmediaapp.model.ShortModel
import com.squareup.picasso.Picasso

class ShortAdapter(var context: Context,var shortList:ArrayList<ShortModel>):RecyclerView.Adapter<ShortAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ShortDesignBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShortDesignBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = shortList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val short = shortList[position]


        if (!short.profileLink.isNullOrEmpty()) {
            Picasso.get().load(short.profileLink).into(holder.binding.profileImage)
        } else {
            // Optionally, you can load a placeholder or default image if the link is empty
            holder.binding.profileImage.setImageResource(R.drawable.profile)  // Replace with your default image resource
        }
        holder.binding.caption.setText(shortList.get(position).caption)
        holder.binding.videoView.setVideoPath(shortList.get(position).postUrl)
        holder.binding.videoView.setOnPreparedListener{
            holder.binding.videoView.start()
        }
    }

}