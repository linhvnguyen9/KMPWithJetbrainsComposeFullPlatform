package presentation.chart

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class LineConfig(
    val hasSmoothCurve: Boolean = false,
    val hasDotMarker: Boolean = false,
    val strokeSize: Dp = 1.dp
)

internal object LineConfigDefaults {

    fun lineConfigDefaults() = LineConfig(
        hasSmoothCurve = true,
        hasDotMarker = true,
        strokeSize = 1.dp
    )
}
