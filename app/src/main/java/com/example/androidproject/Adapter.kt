package com.example.androidproject

import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_loading.view.*
import kotlinx.android.synthetic.main.data_list_item.view.*
import java.util.ArrayList
import android.content.Intent

class Adapter (val context: Context, val itemCheck: (Report) -> Unit , val itemCheck2: (CommuPost) -> Unit)
    : RecyclerView.Adapter<Adapter.ViewHolder>() {


    private var items = ArrayList<Report>()
    private var posts = ArrayList<CommuPost>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {


        val inflater = LayoutInflater.from(viewGroup.context)

        if(itemCheck2==null) { // Report 인경우
            val itemView: View = inflater.inflate(R.layout.data_list_item, viewGroup, false)
            return ViewHolder(itemView, itemCheck,null)
        }
        else { //CommuPost인 경우
            val itemView: View = inflater.inflate(R.layout.commupost__item, viewGroup, false)
            return ViewHolder(itemView, null,itemCheck2)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(itemCheck2==null){
        val item: Report = items[position]
        viewHolder.setItem(item)}
        else{
            val post:CommuPost = posts[position]
        }
    }

    override fun getItemCount(): Int {
        if(itemCheck2==null) {
            return items.count()
        }
        else{return posts.count()}
    }

    fun setItems(items: ArrayList<Report>) {
        this.items = items
    }

    fun setPosts(posts : ArrayList<CommuPost>){
        this.posts = posts
    }



    inner class ViewHolder(itemView: View, itemCheck: ((Report) -> Unit)?, itemCheck2: ((CommuPost) -> Unit)?)
        : RecyclerView.ViewHolder(itemView) {

        fun setItem(item: Report) {
            // url을 다운받기

            itemView.poiid.text = item.Id
            itemView.detail.text = item.DetailLocation
            //firebaseui?.loadWithGlide(item.ImageUrl!!)
            itemView.uploadimg.imageView.setImageURI(item.ImageUrl)
          // Glide.with(itemView)
          //      .load(item.ImageUrl)
            //    .into(itemView.uploadimg)


            itemView.setOnClickListener() { itemCheck(item) }
        }
        fun setPosts(posts: CommuPost){

        }
    }

}