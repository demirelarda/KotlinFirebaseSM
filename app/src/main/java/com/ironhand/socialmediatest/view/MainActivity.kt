package com.ironhand.socialmediatest.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ironhand.socialmediatest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth

        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, FeedsActivity::class.java)
            startActivity(intent)
            finish()
        }

    }



    fun signinClicked(view: View){
        val email = binding.emailText2.text.toString()
        val password = binding.passwordText.text.toString()
        if(email.equals("") || password.equals("")){
            Toast.makeText(this,"Don't leave it blank!",Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                //sucess
                val intent = Intent(this@MainActivity, FeedsActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                //failed
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

    }

    fun signupClicked(view:View){
        val intent = Intent(this@MainActivity, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }



}