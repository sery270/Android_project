package com.akj.anonymoussns

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    // 로그에 TAG 로 사용할 문자열
    val TAG = "MainActivity"
    // 파이어베이스의 test 키를 가진 데이터의 참조 객체를 가져온다.
    val ref = FirebaseDatabase.getInstance().getReference("test")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // actionbar 의 타이틀을 "글목록" 으로 변경
        supportActionBar?.title = "글목록"
        // 하단의 floatingActionButton 이 클릭될때의 리스너를 설정한다.
        floatingActionButton.setOnClickListener {
            // Intent 생성
            val intent = Intent(this@MainActivity, WriteActivity::class.java)
            // Intent 로 WirteActivity 실행
            startActivity(intent)
        }
    }
}
