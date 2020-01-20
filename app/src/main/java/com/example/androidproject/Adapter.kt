package com.example.androidproject

import android.content.Context
import android.net.Uri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.data_list_item.view.*
import java.util.ArrayList
import android.content.Intent

class Adapter (val context: Context, val itemCheck: (Report) -> Unit)
    : RecyclerView.Adapter<Adapter.ViewHolder>() {

    //report형 객체를 담을 배열 선언
    private var items = ArrayList<Report>()

    //view객체를 담는 viewHolder를 만드는 함수
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflater.inflate(R.layout.data_list_item, viewGroup, false)
        return ViewHolder(itemView, itemCheck)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: Report = items[position]
        viewHolder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setItems(items: ArrayList<Report>) {
        this.items = items
    }



    inner class ViewHolder(itemView: View, itemCheck: (Report) -> Unit)
        : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: Report) {
            //view 객체를 만듦
            //해당 화장실의 고유 id 값을 넣음
            itemView.poiid.text = item.Id
            //사용자가 입력한 세부 위치 정보를 넣음
            itemView.detail.text = item.DetailLocation

            //파이어 스토리지에 저장된 이미지에 대한 정보를 가져와서 넣음
            lateinit var imageUrlfromStorage: Uri
            val storage = FirebaseStorage.getInstance()
            var storageRef = storage.getReferenceFromUrl("gs://androidprojectguru.appspot.com").child("images/"+item.ImageUrl)
            storageRef.downloadUrl.addOnCompleteListener {task->
                if(task.isSuccessful){
                    imageUrlfromStorage = task.result!!
                    Glide.with(itemView)
                        .load(imageUrlfromStorage)
                        .into(itemView.uploadimg)
                }
                else {

                }
            }

            itemView.setOnClickListener() { itemCheck(item) }
        }
    }

}