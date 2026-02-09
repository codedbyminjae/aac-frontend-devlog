package com.example.aac

import android.app.Application
import com.example.aac.data.remote.api.RetrofitInstance

class AacApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 앱 시작 시 한 번만 호출
        RetrofitInstance.init(this)
    }
}
