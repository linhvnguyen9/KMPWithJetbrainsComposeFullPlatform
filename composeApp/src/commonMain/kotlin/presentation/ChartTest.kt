package presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import presentation.chart.AxisConfig
import presentation.chart.AxisConfigDefaults
import presentation.chart.ChartDimens
import presentation.chart.ChartDimensDefaults
import presentation.chart.LineConfig
import presentation.chart.LineConfigDefaults
import presentation.chart.LineData
import presentation.chart.dataToOffSetLineChart
import presentation.chart.drawXLabel
import presentation.chart.maxYValue
import presentation.chart.minYValue

@Composable
fun TokenChart(modifier: Modifier = Modifier, sparklineData: List<Double>, color: Color = Color.Green) {
    val minInSparklineData = sparklineData.minOrNull() ?: 0.0
    val processedSparklineData = sparklineData.map { it - minInSparklineData }
    val lineData = convertSparklineToLineData(processedSparklineData)
    LineChart(
        modifier = Modifier.then(modifier),
        color = color,
        lineConfig = LineConfig(hasSmoothCurve = true),
        lineData = lineData
    )
}

fun convertSparklineToLineData(sparkline: List<Double>): List<LineData> {
    return sparkline.mapIndexed { index, price ->
        LineData(index.toFloat(), price.toFloat())
    }
}

@Composable
fun LineChart(
    lineData: List<LineData>,
    color: Color,
    modifier: Modifier = Modifier,
    chartDimens: ChartDimens = ChartDimensDefaults.chartDimesDefaults(),
    axisConfig: AxisConfig = AxisConfigDefaults.axisConfigDefaults(isSystemInDarkTheme()),
    lineConfig: LineConfig = LineConfigDefaults.lineConfigDefaults()
) {
    LineChart(
        lineData = lineData,
        colors = listOf(color, color),
        modifier = modifier,
        chartDimens = chartDimens,
        axisConfig = axisConfig,
        lineConfig = lineConfig
    )
}

@Composable
fun LineChart(
    lineData: List<LineData>,
    colors: List<Color>,
    modifier: Modifier = Modifier,
    chartDimens: ChartDimens = ChartDimensDefaults.chartDimesDefaults(),
    axisConfig: AxisConfig = AxisConfigDefaults.axisConfigDefaults(isSystemInDarkTheme()),
    lineConfig: LineConfig = LineConfigDefaults.lineConfigDefaults()
) {
    val maxYValueState = remember { derivedStateOf { lineData.maxYValue() } }
    val minYValueState = remember { derivedStateOf { lineData.minYValue() } }
    val maxYValue = maxYValueState.value
    val minYValue = minYValueState.value
    val lineBound = remember { mutableStateOf(0F) }

    Canvas(
        modifier = modifier
//            .drawBehind {
//                if (axisConfig.showAxis) {
//                    drawYAxisWithLabels(axisConfig, maxYValue, textColor = axisConfig.textColor)
//                }
//            }
//            .padding(horizontal = chartDimens.padding)

    ) {
        lineBound.value = size.width.div(lineData.count().times(1.2F))
        val scaleFactor = size.height.div(maxYValue)
        val distance = maxYValue/minYValue
        val brush = Brush.linearGradient(colors)
        val radius = size.width.div(70)
        val strokeWidth = lineConfig.strokeSize.toPx()
        val path = Path().apply {
            moveTo(0f, size.height)
        }

        lineData.forEachIndexed { index, data ->
            val centerOffset = dataToOffSetLineChart(index, lineBound.value, size, distance, data, scaleFactor)
            if (lineData.size > 1) {
                when (index) {
                    0 -> path.moveTo(centerOffset.x, centerOffset.y)
                    else -> path.lineTo(centerOffset.x, centerOffset.y)
                }
            }
            if (lineConfig.hasDotMarker) {
                drawCircle(
                    center = centerOffset,
                    radius = radius,
                    brush = brush
                )
            }
//            if (axisConfig.showXLabels) {
//                drawXLabel(
//                    data.xValue,
//                    centerOffset,
//                    radius,
//                    lineData.count(),
//                    axisConfig.textColor
//                )
//            }
        }
        if (lineData.size > 1) {
            val pathEffect =
                if (lineConfig.hasSmoothCurve) PathEffect.cornerPathEffect(strokeWidth) else null
            drawPath(
                path = path,
                brush = brush,
                style = Stroke(width = strokeWidth, pathEffect = pathEffect),
            )
        }
    }
}
