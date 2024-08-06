package presentation.experimental

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.SnapFlingBehavior
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Instant
import presentation.candlechart.Candle
import presentation.candlechart.MarketChartColors

@Composable
fun ExperimentalMarketChart(
    modifier: Modifier,
    candles: List<Candle>,
    marketChartColors: MarketChartColors = MarketChartColors.defaults(),
    textSize: TextUnit = 12.sp,
    dateTransform: (Instant) -> String,
    priceTransform: (Float) -> String
) {
    // TODO: Implement saver
    val state = remember { ExperimentalMarketChartState.getState(candles) }

    val textMeasurer = rememberTextMeasurer()

    Row(modifier) {
        val text = "0.12345"
        val textMeasureResult = textMeasurer.measure(text)

        BoxWithConstraints(Modifier.fillMaxHeight().weight(1f).transformable(state.transformableState)) {
            val chartWidth = constraints.maxWidth.toFloat()
            val chartHeight = constraints.maxHeight - 64.dp.value // Minus the space for the x-axis labels

            state.setViewSize(chartWidth, chartHeight)
            state.calculateGridWidth()

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(marketChartColors.backgroundColor)
            ) {
                clipRect {
                    withTransform({
                        translate(state.offset.x, state.offset.y)
                    }) {
                        state.visibleCandles.forEach { candle ->
                            val xOffset = state.xOffset(candle)
                            val color =
                                if (candle.open > candle.close) marketChartColors.negativeColor else marketChartColors.positiveColor
                            drawLine(
                                color = color,
                                strokeWidth = 2.dp.value,
                                start = Offset(xOffset, state.yOffset(candle.low)),
                                end = Offset(xOffset, state.yOffset(candle.high))
                            )
                            if (candle.open > candle.close) {
                                drawRect(
                                    color = color,
                                    topLeft = Offset(xOffset - state.candleSize / 2, state.yOffset(candle.open)),
                                    size = Size(
                                        state.candleSize, // TODO: Determine size dynamically
                                        state.yOffset(candle.close) - state.yOffset(candle.open)
                                    )
                                )
                            } else {
                                drawRect(
                                    color = color,
                                    topLeft = Offset(xOffset - state.candleSize / 2, state.yOffset(candle.close)),
                                    size = Size(
                                        state.candleSize,
                                        state.yOffset(candle.open) - state.yOffset(candle.close)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        Canvas(
            modifier = Modifier
                .background(Color.Red)
                .width(textMeasureResult.size.width.toDp().dp)
                .fillMaxHeight()
                .scrollable(state.verticalAxisScroll, orientation = Orientation.Vertical, reverseDirection = true)
        ) {
            clipRect {
                withTransform({
                    translate(0f, state.offset.y)
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