package com.example.androidproject

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_report.*

class ReportActivity : AppCompatActivity() {

    private val OPEN_GALLERY =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        ReportActivity_uploadPhoto_Button.setOnClickListener{
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,OPEN_GALLERY)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY){
                var currentImageUrl: Uri?=data?.data

               // try{
                   // val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,currentImageUrl)
                  //  ReportActivity_addPhoto_imageView.setImageB
               // }catch (e:Exception){
                //    e.printStackTrace()
               // }

            }
        }else{
            Log.d("ActivityResult","something wrong")
        }

        ReportActivity_report_Button.setOnClickListener{
            startActivity(Intent(this,FingerPrintActivity::class.java))
        }
    }

}
