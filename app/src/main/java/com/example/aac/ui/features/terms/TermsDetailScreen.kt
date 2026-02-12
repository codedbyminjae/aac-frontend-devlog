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
import com.example.aac.ui.features.auth.AuthViewModel
import androidx.navigation.NavController

@Composable
fun TermsDetailScreen(
    termId: String,
    authViewModel: AuthViewModel,
    navController: NavController
) {

    val termsList by authViewModel.termsList.collectAsState()
    val term = termsList.firstOrNull { it.id == termId }
    var agreed by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        /* ---------- 뒤로가기 ---------- */
        Row(
            modifier = Modifier
                .padding(start = 24.dp, top = 38.dp)
                .clickable {
                    navController.popBackStack()
                },
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

        /* ---------- 본문 ---------- */
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .width(636.dp)
                .padding(top = 125.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = term?.title ?: "",
                fontSize = 32.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .width(636.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFFB2B2B2), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = term?.content ?: "약관을 불러오는 중...",
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            /* ---------- 동의하기 ---------- */
            Box(
                modifier = Modifier
                    .width(636.dp)
                    .height(84.dp)
                    .background(Color(0xFFD7E6F9), RoundedCornerShape(15.dp))
                    .padding(horizontal = 52.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = "${term?.title} 동의하기",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C63A8)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    AgreeCheckBox(
                        checked = agreed,
                        onCheckedChange = {
                            agreed = it
                            if (it) {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("terms_result_$termId", true)

                                navController.popBackStack()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(92.dp))
        }
    }
}
