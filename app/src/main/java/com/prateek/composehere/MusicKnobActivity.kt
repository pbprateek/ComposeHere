package com.prateek.composehere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.prateek.composehere.ui.theme.ComposeHereTheme
import kotlin.math.PI
import kotlin.math.atan2


class MusicKnobActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeHereTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {

                    Box(modifier = Modifier.fillMaxSize()) {
                        MusicKnob(onValueChanged = {
                        })
                    }


                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun MusicKnob(
    modifier: Modifier = Modifier,
    limitingAngle: Float = 0f,
    onValueChanged: (Float) -> Unit
) {
    var rotation by remember {
        mutableStateOf(limitingAngle)
    }

    var touchX by remember {
        mutableStateOf(0f)
    }

    var touchY by remember {
        mutableStateOf(0f)
    }

    var centerX by remember {
        mutableStateOf(0f)
    }

    var centerY by remember {
        mutableStateOf(0f)
    }

    Text(text = "Angle: $rotation", textAlign = TextAlign.Start)

    //A Right angle triangle consists of hypotenuse(H),opposite(O) and adjacent(A) side.
    //And if we have two of above we can calculate the angle of the triangle by using sin, cos or tan
    //sin(@) = O/H , cos(@) = A/H , tan(@) = O/A
    //once we have sin,cos or tan, we can apply arcus(inverse trigonometric) formula to find the exact angle
    //One more thing, these funs gives radian and not degree, so we will have to convert


    Image(
        painter = painterResource(id = R.drawable.music_knob),
        contentDescription = "Music KNob",
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned {
                //There are diff bounds, boundsInRoot gives bound with respect to parent,boundsInWindows gives with respect to screen
                //and boundsInParent gives with respect to parent but also considers clipping if i am not wrong
                val windowBounds = it.boundsInWindow()
                centerX = windowBounds.size.width / 2f
                centerY = windowBounds.size.height / 2f

            }
            .pointerInteropFilter { motionEvent ->
                touchX = motionEvent.x
                touchY = motionEvent.y
                //So wherever we touch ,and we have global coordinates wrt to touch and center of the knob

                //In normal trigonometry, we calculate the angle from the horizontal axis,  but in android, the angle is calculated from the vertical axis.
                //tan(@) = Base/Height
                //Also, in the android coordinate system, the top left corner is (0,0)
                //try to picture it by seeing where the 0 degree is
                val height = touchX - centerX
                val base = centerY - touchY

                // val radianAngle = atan(X/Y)  //This will not work bcz there is a diff between atan and atanx
                //https://stackoverflow.com/questions/283406/what-is-the-difference-between-atan-and-atan2-in-c
                //Check this to know the diff
                //basically atan gives value between -90 to 90, so we can't figure out which quadrant it was in but atan2 gives between -180 to 180
                val radianAngle = atan2(height, base)

                //formula to conver radian to degree
                val degreeAngle = radianAngle * (180f / PI).toFloat()

                rotation = degreeAngle

                true

            }
            .rotate(rotation) //it handles -ve angle properly as it assumes that as counter clock wise so np


    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(color = Color.Red, start = Offset(centerX, centerY), end = Offset(touchX, touchY))
        drawLine(color = Color.Black, start = Offset(centerX, centerY), end = Offset(centerX, 0f))

    }


}
