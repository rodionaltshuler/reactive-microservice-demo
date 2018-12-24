package com.ottamotta.demo

data class TickEvent(val tick : Ticker, val market : String, val timestamp : Long)

data class TickerResponse(val success: Boolean = true, val message: String = "", val result : Ticker)

data class Ticker(
        val Bid: Double = 0.0,
        val Ask: Double = 0.0,
        val Last: Double = 0.0
)

class SampleResponse  {
    val message = "It's a sample response!"
}
