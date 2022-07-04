package com.ironhand.socialmediatest.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ironhand.socialmediatest.databinding.ActivityUploadsBinding
import java.util.*
import kotlin.collections.ArrayList

class UploadsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUploadsBinding
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture: Uri? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage : FirebaseStorage
    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()

        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
        db = Firebase.firestore




    }

    fun imageViewClicked(view: View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galeri İçin İzin Lazım",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver"){
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }else{
                //request permission
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            //start activity for result
            activityResultLauncher.launch(intentToGallery)

        }

    }

    fun uploadClicked(view: View){

        //universal unique id
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName) //images/imageName

        if(selectedPicture != null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {
                //download url -> firestore
                val uploadPictureReference = storage.reference.child("images").child(imageName)
                uploadPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()
                    if(auth.currentUser != null){
                        val username = Firebase.auth.currentUser!!.displayName.toString()
                        val postMap = hashMapOf<String,Any>()
                        postMap.put("downloadUrl",downloadUrl)
                        postMap.put("userEmail",auth.currentUser!!.email!!)
                        postMap.put("username",username)
                        postMap.put("comment",binding.uploadCommentText.text.toString())
                        postMap.put("date",Timestamp.now())
                        val postComments = ArrayList<String>()
                        val commentDetails = ArrayList<String>()
                        postMap.put("postComments",postComments)
                        postMap.put("commentDetails",commentDetails)



                        /*firestore.collection("Posts").add(postMap).addOnSuccessListener {
                            finish()

                        }.addOnFailureListener{
                            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }*/

                        //Get Username from firestore and put it into postMap


                        //upload postmap to firestore
                        firestore.collection("Posts").add(postMap).addOnSuccessListener {
                            finish()

                        }.addOnFailureListener{
                            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                        }

                    }

                }

            }.addOnFailureListener{
                Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }

    }

    //The older version of username system
    /*fun getUserName(){
        db.collection("UserDetails").addSnapshotListener { value, error ->
            if(error!=null){
                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if(value!=null){
                    if(!value.isEmpty){
                        val documents = value.documents
                        for (document in documents){
                            val username = document.get("username")as String
                            usernamez = username
                            println("usr =${usernamez}")

                            //Put username into postMap
                            //postMap.put("username",username) as String
                            //upload postmap to firestore
                            /* firestore.collection("Posts").add(postMap).addOnSuccessListener {
                                 finish()

                             }.addOnFailureListener{
                                 Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                             }*/

                        }
                    }
                }
            }
        }

    } */



    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    intentFromResult.data
                    selectedPicture = intentFromResult.data
                    selectedPicture.let {
                        binding.uploadImageView.setImageURI(it)
                    }
                }
            }

        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if(result){
                //permission granted
                val intentToGallery = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                activityResultLauncher.launch(intentToGallery)
            }else{
                //permission denied
                Toast.makeText(this@UploadsActivity,"İzin Gerekli",Toast.LENGTH_LONG).show()
            }
        }
    }
}