package com.example.androidproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_write.*

class WriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        lateinit var firestore: FirebaseFirestore

        WriteActivity_uploadbtn_Button.setOnClickListener {
            firestore = FirebaseFirestore.getInstance()
            var Post = CommuPost(WriteActivity_title_editview.text.toString(),WriteActivity_content_textview.text.toString())
            firestore?.collection("Board")?.document().set(Post).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(applicationContext,"정상 처리", Toast.LENGTH_SHORT ).show()
                }
            }
        }
    }
}
