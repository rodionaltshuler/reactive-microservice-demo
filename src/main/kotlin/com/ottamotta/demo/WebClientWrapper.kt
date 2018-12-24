package com.ottamotta.demo

import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

class WebClientWrapper(val webClient : WebClient) {

    fun getTicker(market : String) : Mono<Ticker> {
        return webClient
                .get()
                .uri {
                    it.path("/public/getticker")
                            .queryParam("market", market)
                            .build()
                }
                .retrieve()
                .bodyToMono(TickerResponse::class.java)
                .map { it.result }
    }


}