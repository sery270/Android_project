package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //화장실 지도로 연결되는 버튼
        //버튼이 눌리면 MapActivity로 이동
        MainActivity_map_imageView.setOnClickListener{
            startActivity(Intent(this,MapActivity::class.java))

        }

    }
}
