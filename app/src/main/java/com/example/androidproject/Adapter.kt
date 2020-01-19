package com.example.androidproject

import android.content.Context
import android.net.Uri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.data_list_item.view.*
import java.util.ArrayList

class Adapter (val context: Context, val itemCheck: (Report) -> Unit)
    : RecyclerView.Adapter<Adapter.ViewHolder>() {
    private var items = ArrayList<Report>()

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
            // url을 다운받기



            itemView.poiid.text = item.Id
            itemView.detail.text = item.DetailLocation
            //firebaseui?.loadWithGlide(item.ImageUrl!!)
           Glide.with(itemView)
                .load(item.ImageUrl)
                .into(itemView.uploadimg)


            itemView.setOnClickListener() { itemCheck(item) }
        }
    }

}