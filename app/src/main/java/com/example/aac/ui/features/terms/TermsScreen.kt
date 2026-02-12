package com.example.aac.ui.features.terms

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.aac.ui.features.auth.AuthViewModel

@Composable
fun TermsScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    onBackClick: () -> Unit
) {

    // ✅ 시스템(폰) 뒤로가기까지 동일 동작
    BackHandler(enabled = true) {
        onBackClick()
    }

    /* ---------------- 서버 약관 목록 ---------------- */
    val termsList by authViewModel.termsList.collectAsState()

    /* ---------------- 화면 진입 시 약관 조회 ---------------- */
    LaunchedEffect(Unit) {
        authViewModel.fetchTerms()
    }

    /* ---------------- 체크 상태 ---------------- */
    var checkedIds by rememberSaveable { mutableStateOf(setOf<String>()) }

    /* ---------------- 상세 화면 결과 반영 ---------------- */
    val currentBackStackEntry = navController.currentBackStackEntry
    val savedStateHandle = currentBackStackEntry?.savedStateHandle

    LaunchedEffect(savedStateHandle, termsList) {
        termsList.forEach { term ->
            val key = "terms_result_${term.id}"
            val result = savedStateHandle?.get<Boolean>(key)

            if (result == true) {
                checkedIds = checkedIds + term.id
                savedStateHandle.remove<Boolean>(key)
            }
        }
    }

    /* ---------------- 필수 약관 모두 체크 여부 ---------------- */
    val allRequiredChecked = termsList.all { term ->
        !term.isRequired || checkedIds.contains(term.id)
    }

    /* ---------------- 전체 동의 체크 여부 ---------------- */
    val allAgreeChecked = termsList.isNotEmpty() && checkedIds.size == termsList.size

    /* ---------------- UI ---------------- */
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
                .width(636.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

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
                            checkedIds = if (checked) {
                                termsList.map { it.id }.toSet()
                            } else {
                                emptySet()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            /* ---------- 개별 약관 목록 ---------- */
            Box(
                modifier = Modifier
                    .width(535.dp)
                    .background(Color.White, RoundedCornerShape(15.dp))
                    .padding(vertical = 24.dp)
            ) {
                Column {
                    termsList.forEach { term ->

                        TermsAgreeRow(
                            text = term.title,
                            checked = checkedIds.contains(term.id),
                            onCheckedChange = { checked ->
                                checkedIds = if (checked) {
                                    checkedIds + term.id
                                } else {
                                    checkedIds - term.id
                                }
                            },
                            onTextClick = {
                                navController.navigate("terms_detail/${term.id}")
                            }
                        )

                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            /* ---------- 시작하기 ---------- */
            ActionButton(
                text = "시작하기",
                enabled = allRequiredChecked, // 필수만 체크해도 가능
                onClick = {
                    authViewModel.completeSocialSignup()
                }
            )

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
