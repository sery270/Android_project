package com.example.androidproject

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
//import com.google.firebase.referencecode.storage.R
import com.google.firebase.storage.FirebaseStorage

abstract class FirebaseUIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.data_list_item)
    }

    fun loadWithGlide(imageurl:String) {
        // [START storage_load_with_glide]
        // Reference to an image file in Cloud Storage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://androidproject-d4054.appspot.com/"+imageurl)

        // ImageView in your Activity
        val imageView = findViewById<ImageView>(R.id.uploadimg)

        // Download directly from StorageReference using Glide
        // (See MyAppGlideModule for Loader registration)
        Glide.with(this /* context */)
            .load(storageReference)
            .into(imageView)
        // [END storage_load_with_glide]
    }
}