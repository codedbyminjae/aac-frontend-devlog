package com.example.aac.ui.features.terms

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aac.R

@Composable
fun TermsScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit = {},
    onServiceTermsClick: () -> Unit,
    onPrivacyTermsClick: () -> Unit
) {
    /* ---------- 체크 상태 ---------- */
    var serviceChecked by remember { mutableStateOf(false) }
    var privacyChecked by remember { mutableStateOf(false) }

    /* ---------- TermsDetail 결과 수신 ---------- */
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    val serviceResult by savedStateHandle
        ?.getStateFlow("terms_result_service", false)
        ?.collectAsState() ?: remember { mutableStateOf(false) }

    val privacyResult by savedStateHandle
        ?.getStateFlow("terms_result_privacy", false)
        ?.collectAsState() ?: remember { mutableStateOf(false) }

    /* ---------- 결과 반영 ---------- */
    LaunchedEffect(serviceResult) {
        if (serviceResult) serviceChecked = true
    }

    LaunchedEffect(privacyResult) {
        if (privacyResult) privacyChecked = true
    }

    val allAgreeChecked = serviceChecked && privacyChecked

    /* ---------- UI ---------- */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        /* ---------- 상단 뒤로가기 ---------- */
        Row(
            modifier = Modifier
                .padding(start = 24.dp, top = 38.dp)
                .clickable { onBackClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back_circle),
                contentDescription = "Back",
                modifier = Modifier.size(45.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "뒤로가기",
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        /* ---------- 중앙 콘텐츠 ---------- */
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 112.dp)
                .width(636.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /* ---------- 안내 문구 ---------- */
            Text(
                text = buildAnnotatedString {
                    append("약관에 동의하시면\n")
                    withStyle(SpanStyle(color = Color(0xFF1C63A8))) {
                        append("회원가입")
                    }
                    append("이 완료돼요.")
                },
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 36.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(50.dp))

            /* ---------- 전체 동의 ---------- */
            Box(
                modifier = Modifier
                    .width(636.dp)
                    .height(84.dp)
                    .background(
                        color = Color(0xFFD7E6F9),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .padding(horizontal = 52.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "전체 동의",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1C63A8)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    AgreeCheckBox(
                        checked = allAgreeChecked,
                        onCheckedChange = { checked ->
                            serviceChecked = checked
                            privacyChecked = checked
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            /* ---------- 개별 약관 ---------- */
            Box(
                modifier = Modifier
                    .width(535.dp)
                    .height(96.dp)
                    .background(Color.White, RoundedCornerShape(15.dp))
            ) {
                Column(modifier = Modifier.fillMaxSize()) {

                    TermsAgreeRow(
                        text = TermsText.SERVICE_TITLE,
                        checked = serviceChecked,
                        onCheckedChange = { serviceChecked = it },
                        onTextClick = onServiceTermsClick
                    )

                    Spacer(modifier = Modifier.height(30.dp))

                    TermsAgreeRow(
                        text = TermsText.PRIVACY_TITLE,
                        checked = privacyChecked,
                        onCheckedChange = { privacyChecked = it },
                        onTextClick = onPrivacyTermsClick
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            /* ---------- 시작하기 ---------- */
            ActionButton(
                text = "시작하기",
                enabled = allAgreeChecked,
                onClick = { onStartClick() }
            )
        }
    }
}
