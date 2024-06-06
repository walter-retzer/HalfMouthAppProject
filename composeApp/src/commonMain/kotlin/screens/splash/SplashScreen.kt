package screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import halfmouthappproject.composeapp.generated.resources.Res
import halfmouthappproject.composeapp.generated.resources.logohalfmouth
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun SplashScreen(
    onNavigateToLogin: () -> Unit
) {
    val scale = remember { Animatable(0f) }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )
        delay(3000L)
        onNavigateToLogin()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.logohalfmouth),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .scale(scale.value)
                .size(200.dp)
                .clip(RoundedCornerShape(50.dp))
        )
    }
}
