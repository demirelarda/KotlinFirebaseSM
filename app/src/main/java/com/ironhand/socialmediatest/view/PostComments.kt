package com.ironhand.socialmediatest.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ironhand.socialmediatest.adapter.CommentsRecyclerAdapter
import com.ironhand.socialmediatest.adapter.FeedRecyclerAdapter
import com.ironhand.socialmediatest.databinding.ActivityPostCommentsBinding
import com.ironhand.socialmediatest.model.Comment
import com.ironhand.socialmediatest.model.Post


class PostComments : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityPostCommentsBinding
    private lateinit var commentAdapter: CommentsRecyclerAdapter
    private lateinit var commentArrayList: ArrayList<Comment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostCommentsBinding.inflate(layoutInflater)
        val view = binding.root
        binding = ActivityPostCommentsBinding.inflate(layoutInflater)
        setContentView(view)
        binding.recyclerViewComments
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(this)
        auth = Firebase.auth
        db = Firebase.firestore
        commentArrayList = ArrayList<Comment>()
        commentAdapter = CommentsRecyclerAdapter(commentArrayList)
        binding.recyclerViewComments.adapter = commentAdapter


    }


    private fun getCommentData() {

        db.collection("Posts").orderBy(
            "date",
            Query.Direction.DESCENDING
        ).addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
            } else {
                if (value != null) {
                    if (!value.isEmpty) { //is not empty
                        val documents = value.documents
                        for (document in documents) {
                            //casting
                            val postComment = document.get("PostComments") as ArrayList<String>
                            val username = document.get("username") as String
                            val getComment = binding.typeCommentText.text
                            val comment = Comment(postComment,username)
                            //postArrayList.add(post)
                            commentArrayList.add(comment)


                        }
                        commentAdapter.notifyDataSetChanged()
                    }
                }

            }
        }

    }







}




