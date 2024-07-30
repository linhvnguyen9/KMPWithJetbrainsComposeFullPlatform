package presentation.candlechart

import androidx.compose.ui.graphics.Color

data class MarketChartColors(
    val backgroundColor: Color,
    val positiveColor: Color,
    val negativeColor: Color,
    val lineColor: Color,
    val textColor: Color,
    val textOnLastPriceColor: Color
) {
    companion object {
        fun defaults() = MarketChartColors(
            backgroundColor = Color(0xFF182028),
            positiveColor = Color.Green,
            negativeColor = Color.Red,
            lineColor = Color.White,
            textColor = Color.White,
            textOnLastPriceColor = Color.White
        )
    }
}
