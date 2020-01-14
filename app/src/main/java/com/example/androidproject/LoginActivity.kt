package com.example.androidproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var inputid = LoginActivity_id_PlainText.text.toString()
        var inputpasswd = LoginActivity_passwd_Password.text.toString()

        LoginActivity_login_Button.setOnClickListener{

            //startActivity(Intent(this,AdminActivity::class.java))

            startActivity(Intent(this,MainActivity::class.java))
        }

        LoginActivity_join_Button.setOnClickListener{
            startActivity(Intent(this,JoinActivity::class.java))
        }
    }
}
