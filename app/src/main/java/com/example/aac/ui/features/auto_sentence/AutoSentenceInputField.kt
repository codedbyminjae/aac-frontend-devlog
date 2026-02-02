import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 자동 춢력 문장 입력하는 부분
@Composable
fun AutoSentenceInputField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    val borderColor = if (isFocused) Color(0xFF0088FF) else Color(0xFFD9D9D9)
    val borderWidth = if (isFocused) 2.dp else 1.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp) // 🔹 height: 92
            .background(
                color = Color(0xFFFFFFFF),
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 28.dp, vertical = 16.dp) // 🔹 padding-left/right: 28
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            singleLine = false
        )

        // Placeholder
        if (value.isEmpty()) {
            Text(
                text = "자동 출력할 문장을 입력하세요.",
                fontSize = 16.sp,
                color = Color(0xFF9E9E9E)
            )
        }
    }
}
