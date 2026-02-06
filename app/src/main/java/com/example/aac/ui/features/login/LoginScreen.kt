package com.example.aac.ui.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R

// Figma 기준 상단 여백 (1280 x 720 기준)
private const val TITLE_TOP_PADDING_DP = 136

// 버튼 공통 스펙
private val BUTTON_WIDTH = 560.dp
private val BUTTON_HEIGHT = 64.dp
private val BUTTON_RADIUS = 5.dp

@Composable
fun LoginScreen(
    onSocialLoginClick: () -> Unit,
    onGuestLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = TITLE_TOP_PADDING_DP.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "모두와 AAC",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier.size(91.dp)
            )

            Spacer(modifier = Modifier.height(31.dp))

            // 카카오
            LoginButton(
                text = "카카오로 시작하기",
                backgroundColor = Color(0xFFFEE500),
                pressedBackgroundColor = Color(0xFFCCB800),
                textColor = Color(0xFF191600),
                iconRes = R.drawable.ic_kakao,
                onClick = onSocialLoginClick
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 네이버
            LoginButton(
                text = "네이버로 시작하기",
                backgroundColor = Color(0xFF03C75A),
                pressedBackgroundColor = Color(0xFF029744),
                textColor = Color.White,
                iconRes = R.drawable.ic_naver,
                onClick = onSocialLoginClick
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 구글
            LoginButton(
                text = "구글로 시작하기",
                backgroundColor = Color(0xFFF2F2F2),
                pressedBackgroundColor = Color(0xFFD9D9D9),
                textColor = Color(0xFF666666),
                borderColor = Color(0xFFACACAC),
                iconRes = R.drawable.ic_google,
                onClick = onSocialLoginClick
            )

            Spacer(modifier = Modifier.height(9.dp))

            // 로그인 없이 바로 시작하기
            LoginButton(
                text = "로그인 없이 바로 시작하기",
                backgroundColor = Color(0xFFFFFFFF),
                pressedBackgroundColor = Color(0xFFE5E7E9),
                textColor = Color(0xFF1C63A8),
                borderColor = Color(0xFF1C63A8),
                onClick = onGuestLoginClick
            )
        }
    }
}

@Composable
private fun LoginButton(
    text: String,
    backgroundColor: Color,
    pressedBackgroundColor: Color,
    textColor: Color,
    borderColor: Color? = null,
    iconRes: Int? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentBackgroundColor =
        if (isPressed) pressedBackgroundColor else backgroundColor

    Box(
        modifier = Modifier
            .width(BUTTON_WIDTH)
            .height(BUTTON_HEIGHT)
            .then(
                if (borderColor != null) {
                    Modifier.border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(BUTTON_RADIUS)
                    )
                } else Modifier
            )
            .background(
                color = currentBackgroundColor,
                shape = RoundedCornerShape(BUTTON_RADIUS)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            if (iconRes != null) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))
            }

            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )
        }
    }
}
