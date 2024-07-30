package presentation.chart

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

private const val BoundFactor = 1.2F

internal fun dataToOffSet(
    index: Int,
    bound: Float,
    size: Size,
    data: Float,
    yScaleFactor: Float
): Offset {
    val startX = index.times(bound.times(BoundFactor))
    val endX = index.plus(1).times(bound.times(BoundFactor))
    val y = size.height.minus(data.times(yScaleFactor))
    return Offset(((startX.plus(endX)).div(2F)), y)
}

internal fun dataToOffSetLineChart(
    index: Int,
    bound: Float,
    size: Size,
    distance: Float,
    data: LineData,
    yScaleFactor: Float
): Offset {
    val startX = index.times(bound.times(BoundFactor))
    val endX = index.plus(1).times(bound.times(BoundFactor))
    val y = size.height.minus(data.yValue.times(yScaleFactor))

//    val y = size.height / data.yValue * distance
    return Offset(((startX.plus(endX)).div(2F)), y)
}