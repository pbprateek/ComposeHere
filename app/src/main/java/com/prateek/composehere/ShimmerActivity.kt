package com.prateek.composehere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.prateek.composehere.ui.theme.ComposeHereTheme

class ShimmerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        enableEdgeToEdge()
        setContent {
            ComposeHereTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoadingShimmerEffect(modifier = Modifier.padding(innerPadding))
                }
            }
        }


    }
}

@Composable
fun LoadingShimmerEffect(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
        repeat(5) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(16.dp)
                    .shimmerEffect()
            )
        }
    }
}

fun Modifier.shimmerEffect(
    widthOfShadowBrush: Int = 300,
    endOfOffsetY: Float = 80f,
    durationMillis: Int = 1000,
): Modifier = composed {
    val shimmerColors = listOf(
        // 1.
        Color.Transparent,
        Color.Transparent,
        Color.White.copy(alpha = 0.5f),
        Color.White,
        Color.White.copy(alpha = 0.5f),
        Color.Transparent,
        Color.Transparent,
    )

    val transition = rememberInfiniteTransition(label = "shimmerLoadingAnimation")

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val width = with(density) { configuration.screenWidthDp.dp.toPx() }

    val translateAnimation = transition.animateFloat(
        // 2.
        initialValue = 0f,
        targetValue = width + 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "Shimmer loading animation",
    )

    this.background(
        // 3.
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
            end = Offset(x = translateAnimation.value, y = endOfOffsetY),
        ),
    )
}