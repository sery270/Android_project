package com.akj.anonymoussns

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}