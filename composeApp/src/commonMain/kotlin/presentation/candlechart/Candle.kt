package presentation.candlechart

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

@Parcelize
data class Candle(
    val time: Long,
    val open: Float,
    val close: Float,
    val high: Float,
    val low: Float
) : Comparable<Candle>, Parcelable {

    override fun compareTo(other: Candle) = if (time < other.time) -1 else 1
}