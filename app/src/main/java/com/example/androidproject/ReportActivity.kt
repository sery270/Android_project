package com.example.androidproject
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_report.*
import com.example.androidproject.MapActivity
import org.json.JSONObject

class ReportActivity : AppCompatActivity() {

    private val OPEN_GALLERY =1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)


        //갤러리 사진 업로드
        ReportActivity_uploadPhoto_Button.setOnClickListener{
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,OPEN_GALLERY)
        }
        //게시글 정보를 firebase에 업로드
        ReportActivity_report_Button.setOnClickListener{
            val post = ReportPostClass()
            val newRef = FirebaseDatabase.getInstance().getReference("Posts").push()
            post.DetailLocation = ReportActivity_detail_editText.text.toString()
            //post.ImageUrl =
           // post.PoiId =
            //toilet.getString("POI_ID")
            newRef.setValue(post)

            startActivity(Intent(this,FingerPrintActivity::class.java))

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY){
                var currentImageUrl: Uri?=data?.data
                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,currentImageUrl)
                    ReportActivity_uploadphoto_imageView.setImageBitmap(bitmap)
                }catch (e:Exception){
                    e.printStackTrace() }
            }
        }else{
            Log.d("ActivityResult","something wrong")
        }
   }
}
