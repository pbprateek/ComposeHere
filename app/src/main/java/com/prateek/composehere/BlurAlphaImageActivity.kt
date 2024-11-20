package com.prateek.composehere

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prateek.composehere.ui.theme.ComposeHereTheme
import com.prateek.composehere.util.customBlur


class BlurAlphaImageActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val painter = painterResource(id = R.drawable.zane)
            ComposeHereTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(state = rememberScrollState())
                ) {
                    BlurImage()
                    AnimateAlpha()
                }
            }
        }
    }
}


@Composable
fun AnimateAlpha() {
    var visible by remember {
        mutableStateOf(true)
    }
    val animateAlpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        label = "alpha",
        animationSpec = tween(durationMillis = 2000)
    )

    Card(modifier = Modifier) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable {
                    visible = !visible
                }
                .graphicsLayer {
                    alpha = animateAlpha
                },
            painter = painterResource(id = R.drawable.zane),
            contentDescription = "This is Prateek"
        )
        Text(text = "Click me to hide")
    }
}


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun BlurImage() {
    Box {
        Text(text = "Blur Effect")
        Image(
            modifier = Modifier.customBlur(100f),
            painter = painterResource(id = R.drawable.prateek),
            contentDescription = "This is Prateek"
        )
    }

}


@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
private fun BlurPreview() {
    ComposeHereTheme {
        BlurImage()
    }
}



