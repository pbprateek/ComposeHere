package com.prateek.composehere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.dp
import com.prateek.composehere.ui.theme.ComposeHereTheme

class NodeApiActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ComposeHereTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {

                        var enabled by remember { mutableStateOf(true) }
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .fade(enabled)
                            .size(200.dp)
                            .background(Color.Red)
                            .clickable {
                                enabled = !enabled
                            })

                        Spacer(Modifier.height(25.dp))

                        val extendedColors = ExtendedColors(color = Color.Green)
                        CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
                            Text("Color via Compos", color = LocalExtendedColors.current.color)
                        }


                    }
                }
            }
        }
    }
}

//If we do not use "this" ,  the chain will be broken and all previous modifier will be gone

//Chain existing modifiers together
fun Modifier.clip(shape: Shape) = this.graphicsLayer(shape = shape, clip = true)

//Using Modifier
//This has one more prob, Compose Functions with return value are never skipped
//So will recompose on every recomposition
//When we chain modifiers like we did previously are not Composable Function
//So that's a Downside of using Modifier to create new modifier
@Composable
fun Modifier.fade(enable: Boolean): Modifier {
    val alpha by animateFloatAsState(if (enable) 0.5f else 1.0f)
    //Then is an infix to combine two operator one after another
    //Here it is kinda not needed,and can just be done by something like
    //this.graphicsLayer
    return this then Modifier.graphicsLayer { this.alpha = alpha }
}


/*
Passing Value Down to Compose via CompositionLocal
 */
@Immutable
data class ExtendedColors(
    val color: Color
)

//Use StaticCompositionLocalOf when the UI will not update or recomp with change in value
//Basically use it to pass down dependencies in entire compose
//Like LoginService class i want in compose everywhere
val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        color = Color.Unspecified
    )
}


//Node API(Lower Level API for Performance)
// Modifier factory
fun Modifier.circle(color: Color) = this then CircleElement(color)

// ModifierNodeElement
private data class CircleElement(val color: Color) : ModifierNodeElement<CircleNode>() {
    override fun create() = CircleNode(color)

    override fun update(node: CircleNode) {
        node.color = color
    }
}

// Modifier.Node
private class CircleNode(var color: Color) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        drawCircle(color)
    }
}

