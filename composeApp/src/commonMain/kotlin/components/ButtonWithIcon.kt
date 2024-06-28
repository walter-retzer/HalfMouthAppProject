package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.darkScheme
import theme.mainYellowColor

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ButtonWithIcon(
    modifier: Modifier,
    text: String = "",
    textSize: TextUnit = 17.sp,
    drawableResource: DrawableResource,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)

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
        Icon(
            painter = painterResource(drawableResource),
            contentDescription = "icon",
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = 10.dp),
            text = text,
            color = darkScheme.onPrimary,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = textSize,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp
            )
        )
    }
}
