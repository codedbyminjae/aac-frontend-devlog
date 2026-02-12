package com.example.aac.ui.features.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aac.R
import androidx.compose.foundation.clickable

val SideBarGray = Color(0xFF666666)

@Composable
fun SmallReactionButton(
    iconRes: Int,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SideBarGray, RoundedCornerShape(6.dp))
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text,
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CardControlBar(
    onUpClick: () -> Unit,
    onDownClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonColor = Color(0xFF64B5F6)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(buttonColor)
                .clickable { onUpClick() }
        ) {
            Icon(
                painter = painterResource(R.drawable.btn_up),
                contentDescription = "위로",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "위로",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(buttonColor)
                .clickable { onDownClick() }
        ) {
            Icon(
                painter = painterResource(R.drawable.btn_down),
                contentDescription = "아래로",
                modifier = Modifier.size(32.dp),
                tint = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "아래로",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}