package com.example.aac.ui.features.terms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TermsAgreeRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(33.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            lineHeight = 24.sp,
            color = Color(0xFF494949),
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .clickable { onTextClick() }
        )

        Spacer(modifier = Modifier.weight(1f))

        AgreeCheckBox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

