package com.example.aac.feature.ai_sentence.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.aac.R

@Composable
fun SvgButton(
    @DrawableRes resId: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp, // 버튼 크기(피그마에 맞춰 조절)
) {
    Image(
        painter = painterResource(resId),
        contentDescription = contentDescription,
        modifier = modifier
            .size(size)
            .clickable(role = Role.Button, onClick = onClick)
    )
}
