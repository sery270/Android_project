package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_commu.*

class CommuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commu)

        CommuActivity_write_imageView.setOnClickListener{
            startActivity(Intent(this,WriteActivity::class.java))
        }
    }
}
