package com.example.aac

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aac.core.navigation.AppNavGraph
import com.example.aac.ui.theme.AacTheme
import java.security.MessageDigest

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 KeyHash 출력
        printKeyHash()

        // 🔥 딥링크 처리
        handleDeepLink(intent)

        enableEdgeToEdge()

        setContent {
            AacTheme {
                AppNavGraph()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun printKeyHash() {
        try {
            val packageInfo = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )

            val signatures = packageInfo.signingInfo?.apkContentsSigners ?: return

            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val keyHash = Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                Log.d("KeyHash", keyHash)
            }

        } catch (e: Exception) {
            Log.e("KeyHash", "Error printing key hash", e)
        }
    }


    private fun handleDeepLink(intent: Intent?) {
        val uri: Uri = intent?.data ?: return

        Log.d("DeepLink", "uri = $uri")
        Log.d(
            "DeepLink",
            "scheme=${uri.scheme}, host=${uri.host}, path=${uri.path}"
        )

        when (uri.path) {
            "/callback" -> {
                val accessToken = uri.getQueryParameter("accessToken")
                Log.d("DeepLink", "accessToken = $accessToken")
            }

            "/terms" -> {
                val pendingToken = uri.getQueryParameter("pendingToken")
                Log.d("DeepLink", "pendingToken = $pendingToken")
            }
        }
    }
}
