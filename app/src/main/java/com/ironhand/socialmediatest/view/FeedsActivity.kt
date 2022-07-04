package com.ironhand.socialmediatest.view

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ironhand.socialmediatest.R
import com.ironhand.socialmediatest.adapter.FeedRecyclerAdapter
import com.ironhand.socialmediatest.databinding.ActivityFeedsBinding
import com.ironhand.socialmediatest.model.Post
import java.util.*
import kotlin.collections.ArrayList

class FeedsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFeedsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var feedAdapter: FeedRecyclerAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.recyclerView

        auth = Firebase.auth
        db = Firebase.firestore

        postArrayList = ArrayList<Post>()

        getData()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        feedAdapter = FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = feedAdapter




    }

    private fun getData(){
        //Get Post Details
        //where equal to filtrelemek için
        db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value != null){
                    if(!value.isEmpty){ //is not empty
                        val documents = value.documents
                        postArrayList.clear()
                        for(document in documents){
                            //casting
                            val comment = document.get("comment") as String
                            val downloadUrl = document.get("downloadUrl") as String
                            val username = document.get("username") as String

                            val post = Post(("@"+username),comment,downloadUrl)
                            postArrayList.add(post)



                        }
                        feedAdapter.notifyDataSetChanged()
                    }
                }

            }
        }




        }


    fun commentsClicked(view: View){

        val intent = Intent(this, PostComments::class.java)
        startActivity(intent)


    }

    fun likeClicked(view: View){

    }







    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.logmenu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.add_post){
            val intent = Intent(this, UploadsActivity::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.signout){
            auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()

        }else if(item.itemId == R.id.myProfile){
            val intent = Intent(this,ProfileDetailsActivity::class.java)
            startActivity(intent)
        }


        return super.onOptionsItemSelected(item)

    }
}