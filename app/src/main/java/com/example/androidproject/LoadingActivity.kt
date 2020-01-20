package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Thread.sleep(2000)
        }catch (e: InterruptedException ){
            e.printStackTrace()
        }

        startActivity((Intent(this,LoginActivity::class.java)))
    }
}
