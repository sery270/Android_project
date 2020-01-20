package com.example.androidproject

import android.net.Uri

//신고 테이블에 들어갈 필드 값 정의
//즉, Report 컬렉션의 각 doc을 정의하는 필드 정의
data class Report(
    var Id:String?  = null,
    var DetailLocation:String? =null,
    var ImageUrl: String?=null
)