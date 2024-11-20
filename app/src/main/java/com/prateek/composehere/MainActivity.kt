package com.prateek.composehere

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prateek.composehere.ui.theme.ComposeHereTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeHereTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {

                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, ShimmerActivity::class.java))
                        }) {
                            Text("Shimmer Effect")
                        }
                        Spacer(Modifier.height(16.dp))

                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, NodeApiActivity::class.java))
                        }) {
                            Text("Node API")
                        }
                        Spacer(Modifier.height(16.dp))

                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, AnimProgressBarActivity::class.java))
                        }) {
                            Text("AnimProgressBarActivity")
                        }
                        Spacer(Modifier.height(16.dp))

                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, BlurAlphaImageActivity::class.java))
                        }) {
                            Text("Blur")
                        }
                        Spacer(Modifier.height(16.dp))

                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, MusicKnobActivity::class.java))
                        }) {
                            Text("Music Knob")
                        }
                        Spacer(Modifier.height(16.dp))

                        Button(onClick = {
                            startActivity(Intent(this@MainActivity, MusicKnobCompleteActivity::class.java))
                        }) {
                            Text("Music Knob Comp")
                        }
                        Spacer(Modifier.height(16.dp))


                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeHereTheme {
        Greeting("Android")
    }
}