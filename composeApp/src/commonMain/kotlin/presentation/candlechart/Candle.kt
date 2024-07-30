package presentation.candlechart

import kotlinx.datetime.Instant

data class Candle(
    val time: Instant,
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float
) : Comparable<Candle> {

    override fun compareTo(other: Candle) = if (time < other.time) -1 else 1
}