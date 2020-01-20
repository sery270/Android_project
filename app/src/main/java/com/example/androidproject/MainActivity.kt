package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainActivity_map_imageView.setOnClickListener{
            startActivity(Intent(this,MapActivity::class.java))

        }
        MainActivity_commu_imageView.setOnClickListener{
            startActivity(Intent(this,CommunityActivity::class.java))
        }
    }
}
