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
import com.example.aac.ui.theme.AacTheme
import com.example.aac.ui.features.category.CategoryManagementScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AacTheme {
                // 기존 네비게이션은 잠시 주석 처리
                // AppNavGraph()

                // 테스트할 화면을 직접 호출
                CategoryManagementScreen(
                    onBackClick = {
                        // 뒤로가기 클릭 시 로그 확인용 (임시)
                        println("뒤로가기 클릭됨")
                    }
                )
            }
        }
    }
}