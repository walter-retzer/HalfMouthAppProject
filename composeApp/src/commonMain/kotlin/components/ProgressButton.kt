package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.darkScheme
import theme.mainYellowColor


@Composable
fun ProgressButton(
    modifier: Modifier = Modifier,
    text: String = "",
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)

    if (isLoading) {
        Box(modifier = modifier.height(64.dp), contentAlignment = Alignment.Center) {
            MyAppCircularProgressIndicator()
        }
    }

    if (isLoading.not()) {
        Button(
            modifier = modifier
                .height(54.dp)
                .clip(shape)
                .background(
                    brush = Brush.linearGradient(
                        0f to mainYellowColor,
                        1f to mainYellowColor
                    )
                ),
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            onClick = { onClick() },
        ) {
            Text(
                text = text,
                color = darkScheme.onPrimary,
                style = TextStyle(
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    lineHeight = 16.sp,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}
