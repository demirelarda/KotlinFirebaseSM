package com.ironhand.socialmediatest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ironhand.socialmediatest.databinding.RecyclerRowBinding
import com.ironhand.socialmediatest.model.Comment



class CommentsRecyclerAdapter(private val commentList : ArrayList<Comment>):RecyclerView.Adapter<CommentsRecyclerAdapter.CommentHolder>(){


    class CommentHolder(val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CommentHolder(binding)

    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        holder.binding.recyclerCommentText.text = commentList.get(position).userComment.toString()
        //Picasso.get().load(commentList.get(position).downloadUrl).into(holder.binding.recyclerImageView)


    }

    override fun getItemCount(): Int {
        return commentList.size


    }
















}