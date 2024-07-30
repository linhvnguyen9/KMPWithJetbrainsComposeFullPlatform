package presentation.chart

data class LineData(val xValue: Any, val yValue: Float)

fun List<LineData>.maxYValue() = maxOf {
    it.yValue
}

fun List<LineData>.minYValue() = minOf {
    it.yValue
}