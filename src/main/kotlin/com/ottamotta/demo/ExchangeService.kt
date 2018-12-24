package com.ottamotta.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import reactor.core.publisher.Flux
import java.time.Duration

class ExchangeService(val webClient : WebClientWrapper, env : Environment) {

    private var interval = env.getRequiredProperty("exchange.interval").toLong()

    @Value("\${exchange.interval}")
    fun observe(market: String): Flux<TickEvent> {
        return Flux.interval(Duration.ofSeconds(interval))
                .flatMap { webClient.getTicker(market) }
                .map { TickEvent(tick = it, market = market, timestamp = System.currentTimeMillis()) }
    }

}