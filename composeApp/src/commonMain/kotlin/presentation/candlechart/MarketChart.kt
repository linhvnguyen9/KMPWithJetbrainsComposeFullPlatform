package presentation.candlechart

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.hours
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.TextUnit
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun MarketChart(
    modifier: Modifier,
    candles: List<Candle>,
    marketChartColors: MarketChartColors = MarketChartColors.defaults(),
    textSize: TextUnit = 12.sp
) {
    val state =
        rememberSaveable(saver = MarketChartState.Saver) { MarketChartState.getState(candles) }
    val textMeasurer = rememberTextMeasurer()

    BoxWithConstraints(modifier = modifier) {
        val chartWidth = constraints.maxWidth - 128.dp.value
        val chartHeight = constraints.maxHeight - 64.dp.value

        state.setViewSize(chartWidth, chartHeight)
        state.calculateGridWidth()

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .background(marketChartColors.backgroundColor)
                .transformable(state.transformableState)
                .scrollable(state.scrollableState, Orientation.Horizontal)
        ) {
            drawLine(
                color = marketChartColors.lineColor,
                strokeWidth = 2.dp.value,
                start = Offset(0f, chartHeight),
                end = Offset(chartWidth, chartHeight)
            )

            drawLine(
                color = marketChartColors.lineColor,
                strokeWidth = 2.dp.value,
                start = Offset(chartWidth, 0f),
                end = Offset(chartWidth, chartHeight)
            )

            state.timeLines.forEach { candle ->
                val offset = state.xOffset(candle)
                if (offset !in 0f..chartWidth) return@forEach
                drawLine(
                    color = marketChartColors.lineColor,
                    strokeWidth = 1.dp.value,
                    start = Offset(offset, 0f),
                    end = Offset(offset, chartHeight),
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(10f, 20f),
                        phase = 5f
                    )
                )
                val dateTimeFormat = DateTimeComponents.Format {
                    byUnicodePattern("dd.MM, HH:mm")
                }
                val text = candle.time.format(dateTimeFormat)
                val measureResult = textMeasurer.measure(text, TextStyle(fontSize = textSize))
                drawText(
                    textMeasurer,
                    text,
                    Offset(offset - measureResult.size.width / 2, chartHeight + 8.dp.value),
                    TextStyle(fontSize = textSize, color = marketChartColors.textColor)
                )
            }

            state.priceLines.forEach { value: Float ->
                val yOffset = state.yOffset(value)
                val text = value.toString()
                drawLine(
                    color = marketChartColors.lineColor,
                    strokeWidth = 1.dp.value,
                    start = Offset(0f, yOffset),
                    end = Offset(chartWidth, yOffset),
                    pathEffect = PathEffect.dashPathEffect(
                        intervals = floatArrayOf(10f, 20f),
                        phase = 5f
                    )
                )
                val measureResult = textMeasurer.measure(text, TextStyle(fontSize = textSize))
                drawText(
                    textMeasurer,
                    text,
                    Offset(chartWidth + 8.dp.value, yOffset - measureResult.size.height / 2),
                    TextStyle(fontSize = textSize, color = marketChartColors.textColor)
                )
            }

            state.lastCandle?.let {
                val text = it.close.toString()
                val measureResult = textMeasurer.measure(text, TextStyle(fontSize = textSize))

                val color = if (it.open > it.close) marketChartColors.negativeColor else marketChartColors.positiveColor
                val yOffset = state.yOffset(it.close) - measureResult.size.height / 2

                drawRect(
                    color = color,
                    topLeft = Offset(chartWidth + 8.dp.value, yOffset),
                    size = Size(
                        measureResult.size.width.toFloat(),
                        measureResult.size.height.toFloat()
                    )
                )
                drawText(
                    textMeasurer,
                    text,
                    Offset(chartWidth + 8.dp.value, yOffset),
                    TextStyle(fontSize = textSize, color = marketChartColors.textOnLastPriceColor)
                )
            }

            state.visibleCandles.forEach { candle ->
                val xOffset = state.xOffset(candle)
                val color = if (candle.open > candle.close) marketChartColors.negativeColor else marketChartColors.positiveColor
                drawLine(
                    color = color,
                    strokeWidth = 2.dp.value,
                    start = Offset(xOffset, state.yOffset(candle.low)),
                    end = Offset(xOffset, state.yOffset(candle.high))
                )
                if (candle.open > candle.close) {
                    drawRect(
                        color = color,
                        topLeft = Offset(xOffset - 6.dp.value, state.yOffset(candle.open)),
                        size = Size(
                            12.dp.value,
                            state.yOffset(candle.close) - state.yOffset(candle.open)
                        )
                    )
                } else {
                    drawRect(
                        color = color,
                        topLeft = Offset(xOffset - 6.dp.value, state.yOffset(candle.close)),
                        size = Size(
                            12.dp.value,
                            state.yOffset(candle.open) - state.yOffset(candle.close)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MarketChartPreview() {
    MarketChart(
        Modifier.fillMaxWidth().height(400.dp),
        listOf(
            Candle(Clock.System.now().plus(80.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(79.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(78.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(77.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(76.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(75.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(74.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(73.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(72.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(71.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(70.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(69.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(68.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(67.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(66.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(65.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(64.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(63.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(62.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(61.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(60.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(59.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(58.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(57.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(56.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(55.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(54.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(53.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(52.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(51.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(50.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(49.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(48.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(47.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(46.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(45.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(44.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(43.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(42.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(41.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(40.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(39.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(38.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(37.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(36.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(35.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(34.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(33.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(32.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(31.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(30.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(29.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(28.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(27.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(26.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(25.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(24.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(23.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(22.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(21.hours), 16f, 20f, 22f, 15f),
            Candle(Clock.System.now().plus(20.hours), 11f, 16f, 16f, 10f),
            Candle(Clock.System.now().plus(19.hours), 8f, 10f, 11f, 7f),
            Candle(Clock.System.now().plus(18.hours), 6f, 8f, 9f, 5f),
            Candle(Clock.System.now().plus(17.hours), 10f, 6f, 11f, 6f),
            Candle(Clock.System.now().plus(16.hours), 14f, 10f, 15f, 10f),
            Candle(Clock.System.now().plus(15.hours), 12f, 14f, 15f, 10f),
            Candle(Clock.System.now().plus(14.hours), 10f, 12f, 13f, 10f),
            Candle(Clock.System.now().plus(13.hours), 15f, 10f, 16f, 9f),
            Candle(Clock.System.now().plus(12.hours), 14f, 15f, 15f, 12f),
            Candle(Clock.System.now().plus(11.hours), 14f, 14f, 15f, 10f),
            Candle(Clock.System.now().plus(10.hours), 12f, 14f, 15f, 10f),
            Candle(Clock.System.now().plus(9.hours), 10f, 12f, 13f, 10f),
            Candle(Clock.System.now().plus(8.hours), 11f, 10f, 16f, 9f),
            Candle(Clock.System.now().plus(7.hours), 10f, 11f, 12f, 10f),
            Candle(Clock.System.now().plus(6.hours), 6f, 10f, 12f, 4f),
            Candle(Clock.System.now().plus(5.hours), 4f, 6f, 7f, 4f),
            Candle(Clock.System.now().plus(4.hours), 6f, 4f, 8f, 4f),
            Candle(Clock.System.now().plus(3.hours), 3f, 6f, 6f, 2f),
            Candle(Clock.System.now().plus(2.hours), 4f, 3f, 5f, 2f),
            Candle(Clock.System.now().plus(1.hours), 2f, 4f, 6f, 1f)
        ).sorted(),
        marketChartColors = MarketChartColors.defaults().copy(backgroundColor = Color.White, textColor = Color.Black, lineColor = Color.Gray)
    )
}