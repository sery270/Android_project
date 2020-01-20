package com.example.androidproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_report_list.*

class ReportListActivity : AppCompatActivity() {

    private var adapter: Adapter? = null
    private var firestore : FirebaseFirestore? = null

    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)

        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(this) {
            Toast.makeText(this, " 상세위치: ${it.DetailLocation}, poiid: ${it.Id}, image: ${it.ImageUrl}", Toast.LENGTH_SHORT).show()
        }
        recyclerview.adapter = adapter


        id = intent.getStringExtra("id") // 마커클릭시 가져오는 id

        viewDatabase(id)

        ReportListActivity_reportbtn_Button.setOnClickListener{
            //마커 클릭시 가져온 id를 신고페이지로 넘기기 위함
            var intent = Intent(this@ReportListActivity,ReportActivity::class.java)
            intent.putExtra("id",id)

            showSettingPopup()
            //startActivity(intent)

        }
    }

    private fun showSettingPopup() { //채린
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup, null)
        val textView1 : TextView = view.findViewById(R.id.alert_popup_textview1)
        textView1.text = "중복 신고 시 정확한 안전도를 측정하기 어렵습니다."
        val textView2 : TextView = view.findViewById(R.id.alert_popup_textview2)
        textView2.text = "다시 한번 목록을 확인한 후 신고해 주세요."
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("주의하세요!")
//            .setPositiveButton("OK") { dialog, which ->
//                Toast.makeText(applicationContext,"확인하였습니다.",Toast.LENGTH_SHORT).show()
//                startActivity(intent)
//            }
//            .create()

        val butCancel = view.findViewById<Button>(R.id.alert_popup_button1)
        butCancel.setOnClickListener {//닫기 버튼 눌렀을 때
            var intent = Intent(this@ReportListActivity,ReportActivity::class.java)
            intent.putExtra("id",id)
            startActivity(intent)
        }

        alertDialog.setView(view)
        alertDialog.show()
        //alertDialog.window.setLayout(500,400)

//        val butSave = view.findViewById<Button>(R.id.alert_popup_button2)
//        butSave.setOnClickListener {//확인 버튼 눌렀을 때
//            alertDialog.dismiss()
////            Toast.makeText(applicationContext,"화면 전환 됐니",Toast.LENGTH_LONG).show()
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MapActivity::class.java))
    }

    private fun viewDatabase( id:String?) { //onCreate에서 id값을 intent로 가져옴 by sery.
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Reports")?.whereEqualTo("id",id)?.get()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                var List = ArrayList<Report>()
                                for (dc in task.result!!.documents) {
                                    var DTO = dc.toObject(Report::class.java)
                                    List.add(DTO!!)
                                }
                                adapter?.setItems(List)
                                adapter?.notifyDataSetChanged()
                }
                else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}

















