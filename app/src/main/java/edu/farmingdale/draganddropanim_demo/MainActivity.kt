package edu.farmingdale.draganddropanim_demo

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import edu.farmingdale.draganddropanim_demo.ui.theme.DragAndDropAnim_DemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        enableEdgeToEdge()
        setContent {
            DragAndDropAnim_DemoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   DragAndDropBoxes( Modifier.padding(innerPadding))

                }
            }
        }
    }
}

// DONE Analyze the requirements for Individual Project 3
// DONE Show the DragAndDropBoxes composable
// DONE Change the circle to a rect
// DONE Replace the command right with a image or icon
// DONE Make this works in landscape mode only
// DONE Rotate the rect around itself
// DONE Move - translate the rect horizontally and vertically
// DONE Add a button to reset the rect to the center of the screen
// DONE Enable certain animation based on the drop event (like up or down)
// DONE: Make sure to commit for each one of the above and submit this individually



