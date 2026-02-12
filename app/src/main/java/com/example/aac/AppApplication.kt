package com.example.aac

import android.app.Application
import com.example.aac.data.remote.api.RetrofitInstance
import com.kakao.sdk.common.KakaoSdk

class AacApplication : Application() {

    override fun onCreate() {
        super.onCreate()


        KakaoSdk.init(this, "b81add2e841a814fe764875a967552fe") // 카카오 SDK

        // 앱 시작 시 한 번만 호출
        RetrofitInstance.init(this)
    }
}
