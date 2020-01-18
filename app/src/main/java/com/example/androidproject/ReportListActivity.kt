package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_report_list.*


class ReportListActivity : AppCompatActivity() {

    private var adapter: Adapter? = null
    private var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)

        recyclerview.layoutManager = LinearLayoutManager(this)
        adapter = Adapter(this) {
            Toast.makeText(this, " 상세위치: ${it.DetailLocation}, poiid: ${it.Id}", Toast.LENGTH_SHORT).show()
        }
        recyclerview.adapter = adapter

        var id = intent.getStringExtra("id") // 마커클릭시 가져오는 id

        viewDatabase(id)

        ReportListActivity_reportbtn_Button.setOnClickListener{

            //마커 클릭시 가져온 id를 신고페이지로 넘기기 위함
            val intent = Intent(this@ReportListActivity,ReportActivity::class.java)
            val id=intent.putExtra("id",id)

            startActivity(intent)
        }

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

















