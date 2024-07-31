package data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RamOhlcResponse(
    @SerialName("code")
    val code: Int? = 0,
    @SerialName("data")
    val `data`: List<Data?>? = listOf(),
    @SerialName("message")
    val message: String? = ""
) {
    @Serializable
    data class Data(
        @SerialName("close")
        val close: Double? = 0.0,
        @SerialName("date")
        val date: Long? = 0,
        @SerialName("high")
        val high: Double? = 0.0,
        @SerialName("low")
        val low: Double? = 0.0,
        @SerialName("open")
        val `open`: Double? = 0.0,
        @SerialName("usd")
        val usd: Double? = 0.0,
        @SerialName("volume")
        val volume: Long? = 0
    )
}