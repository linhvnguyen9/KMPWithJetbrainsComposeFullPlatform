package presentation.experimental

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer

@Composable
fun CanvasTest(modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        scale *= zoomChange
        offset += panChange
    }

    Row(modifier) {
        BoxWithConstraints(Modifier.fillMaxHeight().weight(1f).transformable(transformableState)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                clipRect {
                    val text = "Hello"
                    val textMeasureResult = textMeasurer.measure(text)
                    // Chart area
                    clipRect(right = constraints.maxWidth.toFloat() - textMeasureResult.size.width) {
                        drawRect(color = Color.Blue)
                        withTransform({
                            scale(scale)
                            translate(offset.x, offset.y)
                        }) {
                            drawText(textMeasurer, text, style = TextStyle(color = Color.White), topLeft = Offset(0f, 0f))
                        }
                    }
                    drawText(textMeasurer, text, style = TextStyle(color = Color.Black), topLeft = Offset(constraints.maxWidth.toFloat() - textMeasureResult.size.width, 0f))
                }
            }
        }

//        BoxWithConstraints(Modifier.fillMaxHeight()) {
//            Canvas(modifier = Modifier.size(maxWidth, maxHeight)) {
//                drawRect(
//                    color = Color.Magenta,
//                )
//                drawText(textMeasurer, "Hello")
//            }
//        }
    }
}

//@Composable
//fun ScrollableMarketChart(modifier: Modifier = Modifier) {
//    val scrollState = rememberScrollState()
//    val textMeasurer = rememberTextMeasurer()
//
//    val canvasHeight = 300.dp
//
////    Row(modifier) {
////        Canvas(
////            modifier = Modifier
////                .background(Color.Blue)
////                .weight(1f)
////                .height(canvasHeight)
////        ) {
////            val scrollOffset = scrollState.value.toFloat()
//////            drawText(
//////                textMeasurer = textMeasurer,
//////                text = "Static Content",
//////                topLeft = Offset( 50f, 100f - scrollOffset)
//////            )
////        }
//
//        BoxWithConstraints(
//            modifier = Modifier
////                .background(Color.Red)
//                .fillMaxSize()
////                .weight(1f)
//                .verticalScroll(scrollState)
//        ) {
//            val chartWidth = constraints.maxWidth.toFloat()
//            val chartHeight = constraints.maxHeight.toFloat()
//
//            Canvas(modifier = Modifier.fillMaxSize()) {
//                val canvasQuadrantSize = size / 2F
//                drawRect(
//                    color = Color.Magenta,
//                    size = canvasQuadrantSize
//                )
//            }
//
////            Canvas(Modifier) {
////                clipRect {
////                    drawText(
////                        textMeasurer,
////                        text = "scrollable text scrollable text scrollable text scrollable text scrollable text",
////                        style = TextStyle(fontSize = 18.sp, color = Color.Black),
////                        topLeft = Offset(0f, 0f)
////                    )
////                }
//////            drawRect(Color.Blue, size = Size(30.dp.value, 30.dp.value))
////
//////            // Draw your content that you want to be scrollable here
//////            val spacingFactor = scrollState.value.toFloat() / 100 + 1
//////            for (i in 1..50) {
//////                drawText(
//////                    textMeasurer = textMeasurer,
//////                    text = "Scrollable Content $i",
//////                    topLeft = Offset(50f, i * 60f * spacingFactor)
//////                )
//////            }
////            }
////        }
//    }
//}