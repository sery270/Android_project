package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_report_list.*
import kotlinx.android.synthetic.main.content_community.*

class CommunityActivity : AppCompatActivity() {

    private var adapter: CommuAdapter? = null
    private var firestore : FirebaseFirestore? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        CommuRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CommuAdapter(this) {
            Toast.makeText(this, " 제목: ${it.title}, 내용: ${it.content}", Toast.LENGTH_SHORT).show()
        }
        CommuRecyclerView.adapter = adapter

        viewDatabase()


        fab.setOnClickListener { view ->
            startActivity(Intent(this,WriteActivity::class.java))
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


    }

    fun viewDatabase(){
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Board")?.get()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var List = ArrayList<CommuPost>()
                    for (dc in task.result!!.documents) {
                        var DTO = dc.toObject(CommuPost::class.java)
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

