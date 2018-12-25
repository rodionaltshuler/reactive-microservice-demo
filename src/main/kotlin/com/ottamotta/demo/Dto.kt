package com.ottamotta.demo

data class TickerResponse(val success: Boolean = true, val message: String = "", val result : Ticker)

data class Ticker(
       val Bid: Double = 0.0,
       val Ask: Double = 0.0,
       val Last: Double = 0.0
)

data class TickerWithTS(val bid: Double = 0.0,
                        val ask: Double = 0.0,
                        val last: Double = 0.0,
                        var ts : Long = 0L
) {
   constructor(tick : Ticker, ts: Long) : this(tick.Bid, tick.Ask, tick.Last) {
        this.ts = ts
   }


}

class SampleResponse  {
    val message = "It's a sample response!"
}
