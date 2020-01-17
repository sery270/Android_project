package com.example.androidproject

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

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PetAdapter(this) {
            Toast.makeText(this, "Breed: ${it.breed}, Age: ${it.age}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        viewDatabase()
    }

    private fun viewDatabase() {
        progressBarView.visibility = View.VISIBLE
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection("Pet3")?.get()
            ?.addOnCompleteListener { task ->
                progressBarView.visibility = View.GONE
                if (task.isSuccessful) {
                    var petList = ArrayList<PetDTO>()
                    for (dc in task.result!!.documents) {
                        var petDTO = dc.toObject(PetDTO::class.java)
                        petList.add(petDTO!!)
                    }
                    adapter?.setItems(petList)
                    adapter?.notifyDataSetChanged()
                }
                else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}

















