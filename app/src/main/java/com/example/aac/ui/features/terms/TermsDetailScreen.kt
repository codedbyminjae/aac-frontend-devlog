package com.example.aac.ui.features.terms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R

@Composable
fun TermsDetailScreen(
    type: String,
    onBackClick: (Boolean) -> Unit
) {
    var agreed by remember { mutableStateOf(false) }

    val title = if (type == "privacy") {
        "개인정보 처리방침"
    } else {
        "서비스 이용약관"
    }

    val agreeText = if (type == "privacy") {
        "개인정보 처리방침 동의하기"
    } else {
        "서비스 이용약관 동의하기"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        /* ---------- 뒤로가기 ---------- */
        Row(
            modifier = Modifier
                .padding(start = 24.dp, top = 38.dp)
                .clickable { onBackClick(agreed) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_circle),
                contentDescription = "Back",
                modifier = Modifier.size(45.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = "뒤로가기", fontSize = 18.sp)
        }

        /* ---------- 스크롤 영역 ---------- */
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .width(636.dp)
                .padding(top = 125.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- 약관 본문 ---------- */
            Box(
                modifier = Modifier
                    .width(636.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFFB2B2B2), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                if (type == "privacy") {
                    PrivacyTermsContent()
                } else {
                    ServiceTermsContent()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- 동의하기 ---------- */
            Box(
                modifier = Modifier
                    .width(636.dp)
                    .height(84.dp)
                    .background(Color(0xFFD7E6F9), RoundedCornerShape(15.dp))
                    .padding(start = 52.dp, end = 50.dp, top = 23.dp, bottom = 23.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = agreeText,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C63A8)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    AgreeCheckBox(
                        checked = agreed,
                        onCheckedChange = {
                            agreed = it
                            if (it) {
                                onBackClick(true)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(92.dp))
        }
    }
}
