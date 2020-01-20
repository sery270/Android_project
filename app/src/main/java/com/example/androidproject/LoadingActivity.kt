package com.example.androidproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity

class LoadingActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //앱이 시작될 때, 여심저격 로고를 약 2초간 보여줌
        super.onCreate(savedInstanceState)
        try {
            Thread.sleep(2000)
        }catch (e: InterruptedException ){
            e.printStackTrace()
        }

        startActivity((Intent(this,LoginActivity::class.java)))
    }
}
