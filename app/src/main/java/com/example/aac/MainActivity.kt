//package com.example.aac
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import com.example.aac.core.navigation.AppNavGraph
//import com.example.aac.ui.theme.AacTheme
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        enableEdgeToEdge()
//
//        setContent {
//            AacTheme {
//                AppNavGraph()
//            }
//        }
//    }
//}

package com.example.aac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// import com.example.aac.core.navigation.AppNavGraph // 잠시 주석 처리
import com.example.aac.ui.theme.AacTheme
import com.example.aac.ui.features.speak_setting.SpeakSettingScreen // [중요] import 추가

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AacTheme {

                SpeakSettingScreen(
                    onBackClick = {
                    },
                    onSaveClick = { count ->
                        println("선택된 열 개수: $count")
                    }
                )
            }
        }
    }
}