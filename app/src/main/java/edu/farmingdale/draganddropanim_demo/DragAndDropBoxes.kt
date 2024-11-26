@file:OptIn(ExperimentalFoundationApi::class)

package edu.farmingdale.draganddropanim_demo

import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun DragAndDropBoxes(modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize()) {
        var selectedAnimation by remember { mutableStateOf("rotate") }
        var moveX by remember { mutableStateOf(0f) }
        var moveY by remember { mutableStateOf(0f) }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.2f)
        ) {
            val boxCount = 4
            var dragBoxIndex by remember { mutableIntStateOf(0) }

            repeat(boxCount) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(10.dp)
                        .border(1.dp, Color.Black)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = remember {
                                object : DragAndDropTarget {
                                    override fun onDrop(event: DragAndDropEvent): Boolean {
                                        moveX = when (index) {
                                            0 -> +30f
                                            1 -> -30f
                                            else -> +0f            //movement logic
                                        }
                                        moveY = when (index) {
                                            2 -> +30f
                                            3 -> -30f
                                            else -> +0f
                                        }
                                        dragBoxIndex = index
                                        return true
                                    }
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    this@Row.AnimatedVisibility(
                        visible = index == dragBoxIndex,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icon),
                            contentDescription = "My Image",
                            modifier = Modifier
                                .background(Color.White)
                                .fillMaxSize()
                                .dragAndDropSource {
                                    detectTapGestures(
                                        onLongPress = { offset ->
                                            startTransfer(
                                                transferData = DragAndDropTransferData(
                                                    clipData = ClipData.newPlainText(
                                                        "text",
                                                        ""
                                                    )
                                                )
                                            )
                                        }
                                    )
                                }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.8f)
                .background(Color.Magenta)
        ) {

            val fadeInOut = rememberInfiniteTransition()
            val fadeIt by fadeInOut.animateFloat(
                initialValue = 1f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                )
            )

            val repeatRotate = rememberInfiniteTransition()
            val rotationAngle by repeatRotate.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )

            val stretchSize by animateFloatAsState(
                targetValue = 2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(
                modifier = Modifier       //the button that will be moving around
                    .size(60.dp)
                    .offset(moveX.dp, moveY.dp)
                    .graphicsLayer {
                        when (selectedAnimation) {
                            "rotate" -> rotationZ = rotationAngle
                            "stretch" -> {
                                scaleX = stretchSize
                                scaleY = stretchSize
                            }
                            "fade" -> alpha = fadeIt
                        }
                    }
                    .clip(RoundedCornerShape(5))
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(5))
                    .background(Color.Yellow)
                    .align(Alignment.Center)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Button(
                        onClick = { moveX -= 30f },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                        modifier = Modifier
                            .border(4.dp, Color.White, shape = RoundedCornerShape(50))
                            .clip(RoundedCornerShape(50))
                    ) {
                        Text("Left")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { moveX += 30f },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                        modifier = Modifier
                            .border(4.dp, Color.White, shape = RoundedCornerShape(50))
                            .clip(RoundedCornerShape(50))
                    ) {
                        Text("Right")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { moveY -= 30f },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                        modifier = Modifier
                            .border(4.dp, Color.White, shape = RoundedCornerShape(50))
                            .clip(RoundedCornerShape(50))
                    ) {
                        Text("Up")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { moveY += 30f },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                        modifier = Modifier
                            .border(4.dp, Color.White, shape = RoundedCornerShape(50))
                            .clip(RoundedCornerShape(50))
                    ) {
                        Text("Down")
                    }
                }
                                //button is outside the scope so it appears to the far right
                Button(
                    onClick = {
                        moveX = 0f
                        moveY = 0f
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .border(4.dp, Color.White, shape = RoundedCornerShape(50))
                        .clip(RoundedCornerShape(50))
                ) {
                    Text("Reset")
                }
            }
        }
    }
}
