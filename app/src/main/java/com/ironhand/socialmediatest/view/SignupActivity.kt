package com.ironhand.socialmediatest.view


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ironhand.socialmediatest.R
import com.ironhand.socialmediatest.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
        db = Firebase.firestore


    }

    fun signup(view:View){
        val email = binding.emailText.text.toString()
        val username = binding.usernameText2.text.toString()
        val password = binding.passwordText2.text.toString()
        val birthdate = binding.dateText.text.toString()
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(this,"Don't Leave them empty!",Toast.LENGTH_LONG).show()
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val currentuser = Firebase.auth.currentUser
                val profileUpdates = userProfileChangeRequest {
                    displayName = username
                }

                currentuser!!.updateProfile(profileUpdates).addOnCompleteListener{
                    if(it.isSuccessful){
                        println("username = ${username}")
                    }
                }
                val postMap = hashMapOf<String,Any>()
                postMap.put("username",username)
                postMap.put("password",password)
                postMap.put("email",email)
                postMap.put("birthdate",birthdate)




                firestore.collection("UserDetails").add(postMap).addOnSuccessListener {
                    finish()



                }.addOnFailureListener{
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }



                //sucess
                val intent = Intent(this@SignupActivity,FeedsActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                //failed
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

    }

}