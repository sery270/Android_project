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
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.json.JSONObject
import java.io.File
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast
import androidx.core.content.res.ColorStateListInflaterCompat
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.android.MarkerManager
import java.text.SimpleDateFormat
import java.util.*


class ReportActivity : AppCompatActivity() {
    lateinit var firestore: FirebaseFirestore
    private val OPEN_GALLERY =1
    lateinit var imageFromView : Uri
    lateinit var imageUrlfromStorage: Uri

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
            //이미지 처리
            // 참조 만들기
            val storage = FirebaseStorage.getInstance()
            var storageRef = storage.reference

            var formatter:SimpleDateFormat = SimpleDateFormat("yyyyMMHH_mmss")
            var now:Date = Date()
            var filename:String = formatter.format(now) + ".jpg"

            storageRef = storage.getReferenceFromUrl("gs://androidproject-d4054.appspot.com/").child("images/"+filename)
            val uploadTask = storageRef.putFile(imageFromView)
          //  imageUrlfromStorage = storageRef.downloadUrl.result!!

            // Storage 에서 방금 넣은 파일 url 가져오기
            val urlTask = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                storageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    imageUrlfromStorage = task.result!!
                } else {
                    // Handle failures
                    // ...
                }
            }

            //데이터를 firestore로 보내는 함수 호출
            addReport(imageUrlfromStorage)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK){
            if(requestCode == OPEN_GALLERY){
                var currentImageUrl: Uri?=data?.data
                imageFromView = currentImageUrl!!
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

    //신고 버튼을 누르면 실행되는 함수
    //신고 내용이 firestore에 저장됨
    private fun addReport(url: Uri){
        if(ReportActivity_addr_textView.text.toString().isEmpty()){
            Toast.makeText(applicationContext,"입력을 완성해주세요",Toast.LENGTH_SHORT ).show()
        }
        //var report = Report(intent.getStringExtra("id"), ReportActivity_detail_editText.text.toString(),"s")
        var report = Report(intent.getStringExtra("id"),ReportActivity_detail_editText.text.toString(),url)

        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Reports")?.document()?.set(report).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(applicationContext,"신고 정상 처리",Toast.LENGTH_SHORT ).show()
            }
        }


    }
}
