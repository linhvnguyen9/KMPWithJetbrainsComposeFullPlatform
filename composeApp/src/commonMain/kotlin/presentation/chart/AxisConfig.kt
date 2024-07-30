package presentation.chart

import androidx.compose.ui.graphics.Color

data class AxisConfig(
    val showAxis: Boolean,
    val isAxisDashed: Boolean,
    val showUnitLabels: Boolean,
    val showXLabels: Boolean,
    val xAxisColor: Color = Color.LightGray,
    val yAxisColor: Color = Color.LightGray,
    val textColor: Color,
)

internal object AxisConfigDefaults {

    fun axisConfigDefaults(isDarkMode: Boolean) = AxisConfig(
        xAxisColor = Color.LightGray,
        showAxis = false,
        isAxisDashed = false,
        showUnitLabels = false,
        showXLabels = false,
        yAxisColor = Color.LightGray,
        textColor = if (isDarkMode) Color.White else Color.Black
    )
}
