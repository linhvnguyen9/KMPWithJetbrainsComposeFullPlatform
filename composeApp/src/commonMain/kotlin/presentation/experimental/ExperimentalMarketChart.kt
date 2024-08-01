package presentation.experimental

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.key.Key.Companion.P
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun CanvasTest(modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var verticalAxisOffset by remember { mutableStateOf(0f) }

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        scale *= zoomChange
        offset += panChange
    }

    val verticalAxisTransformableState = TransformableState { zoomChange, panChange, _ ->
        verticalAxisOffset += panChange.y
    }

    Row(modifier) {
        val text = "0.12345"
        val textMeasureResult = textMeasurer.measure(text)

        BoxWithConstraints(Modifier.fillMaxHeight().weight(1f).transformable(transformableState)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                clipRect {
                    drawRect(color = Color.Blue)
                    withTransform({
                        translate(offset.x, offset.y)
                    }) {
                        drawText(textMeasurer, text, style = TextStyle(color = Color.White), topLeft = Offset(0f, 0f))
                        drawLine(Color.White, start = Offset(0f, textMeasureResult.size.height.toFloat() / 2), end = Offset(size.maxDimension, textMeasureResult.size.height.toFloat() / 2), strokeWidth = 2f)
                    }
                }
            }
        }

        Canvas(
            modifier = Modifier
                .background(Color.Red)
                .width(textMeasureResult.size.width.toDp().dp)
                .fillMaxHeight().transformable(verticalAxisTransformableState)
        ) {
            clipRect {
                withTransform({
                    translate(0f, offset.y)
                }) {
                    drawText(textMeasurer, text, topLeft = Offset(0f, 0f))
                }
            }
        }
    }
}

// Helper function to convert Int to Dp
@Composable
private fun Int.toDp() = this / LocalDensity.current.density