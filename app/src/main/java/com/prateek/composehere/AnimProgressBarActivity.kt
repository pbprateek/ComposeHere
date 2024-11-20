package com.prateek.composehere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prateek.composehere.ui.theme.ComposeHereTheme
import kotlinx.coroutines.delay

class AnimProgressBarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeHereTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        var percent by remember {
                            mutableStateOf(0.7f)
                        }

                        //CircularProgressBar(percentage = percent, number = 100)
                        CircularProgressBarAlternative(percentage = percent, number = 100)
                        LaunchedEffect(key1 = true) {
                            delay(4000)
                            percent = 0.1f

                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CircularProgressBar(
    percentage: Float,
    number: Int,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,
    color: Color = Color.Green,
    stokeWidth: Dp = 8.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {

    var animationPlayed by remember { mutableStateOf(false) }
    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration, delayMillis = animDelay
        )
    )
    //Used to call suspend func inside compose, Will cancel and relaunch if key changes
    //Below we are using it just to set animationPlayed as  true for one time, won't trigger on recomposition, So even if percent changes from 0.8 to .1 ,
    // it will not start again from 0 but will look like progress in decreasing
    //(There must be other ways, seems overkill)
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * curPercentage.value,
                useCenter = false,
                style = Stroke(width = stokeWidth.toPx(), cap = StrokeCap.Round),
                //   blendMode = BlendMode.Darken
            )
        }

        Text(
            text = (curPercentage.value * number).toInt().toString(),
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )


    }

}


@Composable
fun CircularProgressBarAlternative(
    percentage: Float,
    number: Int,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,
    color: Color = Color.Green,
    stokeWidth: Dp = 8.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {

    var currentPercent = remember { Animatable(0f) }
    LaunchedEffect(key1 = percentage) {
        //animateTo is a suspend func
        //So with every change in percent, it will call the suspend func again which will eventually change the currentPercent state and will led to change in UI
        currentPercent.animateTo(
            targetValue = percentage,
            animationSpec = tween(
                durationMillis = animDuration, delayMillis = animDelay
            )
        )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * currentPercent.value,
                useCenter = false,
                style = Stroke(width = stokeWidth.toPx(), cap = StrokeCap.Round),
                //   blendMode = BlendMode.Darken
            )
        }

        Text(
            text = (currentPercent.value * number).toInt().toString(),
            color = Color.White,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )


    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    ComposeHereTheme {

    }
}