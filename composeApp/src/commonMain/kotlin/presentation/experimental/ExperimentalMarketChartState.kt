package presentation.experimental

import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import presentation.candlechart.Candle
import kotlin.math.roundToInt

class ExperimentalMarketChartState {

    private var candles = listOf<Candle>()
    private var visibleCandleCount by mutableStateOf(0)
    var offset by mutableStateOf(Offset.Zero)
        private set
    private var viewWidth = 0f
    private var viewHeight = 0f
    private var candleInGrid = Float.MAX_VALUE
    var candleSize = 12.dp.value
        private set
    private var autoFit by mutableStateOf(false)
    var verticalAxisOffset by mutableStateOf(1f)

    private val maxPrice by derivedStateOf { visibleCandles.maxOfOrNull { it.high } ?: 0f }
    private val minPrice by derivedStateOf { visibleCandles.minOfOrNull { it.low } ?: 0f }

    val transformableState = TransformableState { zoomChange, panChange, _ ->
        val offsetY = if (autoFit) 0f else offset.y + panChange.y
        val offsetX = if (panChange.x > 0) {
            (offset.x - panChange.x.scrolledCandles).coerceAtLeast(0f)
        } else {
            (offset.x - panChange.x.scrolledCandles).coerceAtMost(candles.lastIndex.toFloat())
        }

        offset = Offset(offsetX, offsetY)
        scaleView(zoomChange)
    }

    val verticalAxisScroll = ScrollableState {
        verticalAxisOffset += it / VERTICAL_SCALE_DAMPING
        it / VERTICAL_SCALE_DAMPING
    }

    private val Float.scrolledCandles: Float
        get() = this * visibleCandleCount.toFloat() / viewWidth

    val visibleCandles by derivedStateOf {
        if (candles.isNotEmpty()) {
            candles.subList(
                offset.x.roundToInt().coerceAtLeast(0),
                (offset.x.roundToInt() + visibleCandleCount).coerceAtMost(candles.size)
            )
        } else {
            emptyList()
        }
    }

    val lastCandle by derivedStateOf {
        visibleCandles.lastOrNull()
    }

    val latestCandle by derivedStateOf {
        candles.lastOrNull()
    }

    private fun scaleView(zoomChange: Float) {
        if ((zoomChange < 1f && visibleCandleCount / zoomChange <= MAX_CANDLES) ||
            (zoomChange > 1f && visibleCandleCount / zoomChange >= MIN_CANDLES)
        ) {
            visibleCandleCount = (visibleCandleCount / zoomChange).roundToInt()
        }
    }

    fun setViewSize(width: Float, height: Float) {
        viewWidth = width
        viewHeight = height
    }

    fun calculateGridWidth() {
        val candleWidth = viewWidth / visibleCandleCount
        val currentGridWidth = candleInGrid * candleWidth
        when {
            currentGridWidth < MIN_GRID_WIDTH -> {
                candleInGrid = MAX_GRID_WIDTH / candleWidth
//                timeLines = candles.filterIndexed { index, _ -> index % candleInGrid.roundToInt() == 0 }
            }

            currentGridWidth > MAX_GRID_WIDTH -> {
                candleInGrid = MIN_GRID_WIDTH / candleWidth
//                timeLines = candles.filterIndexed { index, _ -> index % candleInGrid.roundToInt() == 0 }
            }
        }
    }

    fun xOffset(candle: Candle) =
        viewWidth * visibleCandles.indexOf(candle).toFloat() / visibleCandleCount.toFloat()

    fun yOffset(value: Float) = if (autoFit) {
        viewHeight * (maxPrice - value) / (maxPrice - minPrice)
    } else {
        viewHeight * value * verticalAxisOffset.coerceAtLeast(VERTICAL_SCALE_MIN)
    }

    companion object {
        private const val MAX_CANDLES = 100 // TODO: Make a system so that we don't depend on max and min candles
        private const val MIN_CANDLES = 30
        private const val MAX_GRID_WIDTH = 500 // TODO: Make a system so that we don't depend on max and min width
        private const val MIN_GRID_WIDTH = 250
        private const val START_CANDLES = 60
        private const val VERTICAL_SCALE_DAMPING = 1500
        private const val VERTICAL_SCALE_MIN = 0.05f

        fun getState(
            candles: List<Candle>,
            visibleCandleCount: Int? = null,
            scrollOffset: Float? = null
        ): ExperimentalMarketChartState {
            return ExperimentalMarketChartState().apply {
                this.candles = candles
                this.visibleCandleCount = visibleCandleCount ?: START_CANDLES
            }
        }
    }
}