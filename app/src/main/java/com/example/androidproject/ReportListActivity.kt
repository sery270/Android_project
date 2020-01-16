package com.example.androidproject

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_report_list.*


class ReportListActivity : AppCompatActivity() {

    private var adapter: PostAdapter? = null
    private var firebase : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_list)




        ReportListActivity_listView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(this) {
        }
        ReportListActivity_listView.adapter = adapter

       // viewDatabase()
    }



}

















