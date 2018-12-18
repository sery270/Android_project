package com.akj.anonymoussns

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_write.*
import kotlinx.android.synthetic.main.card_background.view.*

class WriteActivity : AppCompatActivity() {
    /**
     * 배경 리스트 데이터
     * res/drawable 디렉토리에 있는 배경 이미지를 uri 주소로 사용한다.
     * uri 주소로 사용하면 추후 웹에 있는 이미지 URL 도 바로 사용이 가능하다.
     */
    val bgList = mutableListOf(
            "android.resource://[패키지이름을 넣으세요]/drawable/default_bg"
            , "android.resource://[패키지이름을 넣으세요]/drawable/bg2"
            , "android.resource:/[패키지이름을 넣으세요]/drawable/bg3"
            , "android.resource://[패키지이름을 넣으세요]/drawable/bg4"
            , "android.resource://[패키지이름을 넣으세요]/drawable/bg5"
            , "android.resource://[패키지이름을 넣으세요]/drawable/bg6"
            , "android.resource://[패키지이름을 넣으세요]/drawable/bg7"
            , "android.resource://[패키지이름을 넣으세요]/drawable/bg8"
            , "android.resource://[패키지이름을 넣으세요]/drawable/bg9"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)
        // actionbar 의 타이틀을 "글쓰기" 로 변경
        supportActionBar?.title = "글쓰기"
        // recyclerView 에서 사용할 레이아웃 매니저를 생성한다.
        val layoutManager = LinearLayoutManager(this@WriteActivity)
        // recyclerView 를 횡으로 스크롤 할것이므로 layoutManager 의 방향을 HORIZONTAL 로 설정한다.
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        // recyclerView 에 레이아웃 매니저를 방금 생성한 layoutManger 로 설정한다.
        recyclerView.layoutManager = layoutManager
        // recyclerView 에 adapter 를 설정한다.
        recyclerView.adapter = MyAdapter()
    }

    // RecyclerView 에서 사용하는 View 홀더 클래스
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.imageView
    }

    // RecyclerView 의 어댑터 클래스
    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        // RecyclerView 에서 각 Row(행)에서 그릴 ViewHolder 를 생성할때 불리는 메소드
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            // RecyclerView 에서 사용하는 ViewHolder 클래스를 card_background 레이아웃 리소스 파일을 사용하도록 생성한다.
            return MyViewHolder(LayoutInflater.from(this@WriteActivity).inflate(R.layout.card_background, parent, false))
        }

        // RecyclerView 에서 몇개의 행을 그릴지 기준이 되는 메소드
        override fun getItemCount(): Int {
            //
            return bgList.size
        }

        // 각 행의 포지션에서 그려야할 ViewHolder UI 에 데이터를 적용하는 메소드
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            // 이미지 로딩 라이브러리인 피카소 객체로 뷰홀더에 존재하는 imageView 에 이미지 로딩
            Picasso.get()
                    .load(Uri.parse(bgList[position]))
                    .fit()
                    .centerCrop()
                    .into(holder.imageView)
            // 각 배경화면 행이 클릭된 경우에 이벤트 리스너 설정
            holder.itemView.setOnClickListener {
                // 이미지 로딩 라이브러리인 피카소 객체로 뷰홀더에 존재하는 글쓰기 배경 이미지뷰에 이미지 로딩
                Picasso.get()
                        .load(Uri.parse(bgList[position]))
                        .fit()
                        .centerCrop()
                        .into(writeBackground)
            }
        }
    }
}
