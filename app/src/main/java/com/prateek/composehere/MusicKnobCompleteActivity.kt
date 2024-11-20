package com.prateek.composehere

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.prateek.composehere.ui.theme.ComposeHereTheme
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.roundToInt

class MusicKnobCompleteActivity : ComponentActivity() {

    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeHereTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .border(1.dp, Color.Green, RoundedCornerShape(10.dp))
                                .padding(30.dp)
                        ) {
                            var volume by remember {
                                mutableStateOf(0f)
                            }
                            val barCount = 30
                            MusicKnobFinal(
                                modifier = Modifier.size(100.dp)
                            ) {
                                volume = it
                            }
                            Spacer(modifier = Modifier.width(20.dp))
                            VolumeBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp),
                                //10*1 = 10, 10 *0.2 = 2
                                activeBars = (barCount * volume).roundToInt(),
                                barCount = barCount
                            )
                        }
                    }
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun MusicKnobFinal(
    modifier: Modifier = Modifier,
    limitingAngle: Float = 25f,
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

                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE -> {
                        if (degreeAngle !in -limitingAngle..limitingAngle) {

                            val fixedAngle = if (degreeAngle in -180f..-limitingAngle) {
                                360f + degreeAngle //for -ve angles
                            } else {
                                degreeAngle
                            }
                            rotation = fixedAngle

                            val percent = (fixedAngle - limitingAngle) / (360f - 2 * limitingAngle)
                            onValueChanged(percent)
                            true
                        } else false
                    }

                    else -> false
                }
            }
            .rotate(rotation) //it handles -ve angle properly as it assumes that as counter clock wise so np
    )
}

@Composable
fun VolumeBar(
    modifier: Modifier = Modifier,
    activeBars: Int = 0,
    barCount: Int = 10
) {

    //minWidth, maxWidth, minHeight, maxHeight u get as extra compared to box
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val barWidth = remember {
            //barCount *2 bcz if bar count is 30, we will have to fit 60 bars in the width to achieve the design(the remaining 30 are just for spaces and we are not gonna
            // draw them but we will leave space)
            constraints.maxWidth / (2f * barCount)
        }
        Canvas(modifier = modifier) {
            for (i in 0 until barCount) {
                drawRoundRect(
                    color = if (i in 0..activeBars) Color.Green else Color.DarkGray,
                    //topLeft = Offset((i * barWidth * 2f) , 0f),the prob with this is, if u follow, in the end there will be an space equal to one bar
                    //So just to make it look centered we add half bar length in the start
                    topLeft = Offset((i * barWidth * 2f) + barWidth / 2f, 0f),
                    size = Size(
                        barWidth, constraints.maxHeight.toFloat()
                    ),
                    cornerRadius = CornerRadius(0f)
                )
            }
        }
    }
}