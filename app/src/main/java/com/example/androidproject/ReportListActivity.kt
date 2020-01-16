package com.example.androidproject

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_report_list.*


class ReportListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)

        val listView: MutableList<ReportPostClass> = mutableListOf()

        FirebaseDatabase.getInstance().getReference("/Posts").addChildEventListener(object :
        ChildEventListener {
        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            p0?.let { p0->
               val data = p0.getValue(ReportPostClass::class.java)
               data?.let {
                   if(p1 == null){
                    listView.add(it)
                      // textView.text=p0.child("")
                       textView.text = data.DetailLocation


                   }
               }

                }
        }
        override fun onCancelled(p0: DatabaseError) {
        }
        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        }
        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        }
        override fun onChildRemoved(p0: DataSnapshot) {
        }
    })


        }
}
