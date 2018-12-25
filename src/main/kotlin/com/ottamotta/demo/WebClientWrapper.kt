package com.ottamotta.demo

import org.slf4j.LoggerFactory
import org.springframework.core.env.Environment
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.time.Duration

open class WebClientWrapper(val webClient: WebClient,
                            val redis: ReactiveRedisOperations<String, Ticker>,
                            val expire : Duration) {

    val LOGGER = LoggerFactory.getLogger(WebClientWrapper::class.java.simpleName)

    open fun getTicker(market: String): Mono<Ticker> {
        return redis.opsForValue()[market]
                .doOnSuccess { LOGGER.info("Got tick from cache ${it?.toString()}") }
                .switchIfEmpty(
                        webClient
                                .get()
                                .uri {
                                    it.path("/public/getticker")
                                            .queryParam("market", market)
                                            .build()
                                }
                                .retrieve()
                                .bodyToMono(TickerResponse::class.java)
                                .map { it.result }
                                .doOnSuccess {
                                    LOGGER.info("Saving tick to cache for market $market ${it}")
                                    redis.opsForValue().set(market, it, expire).subscribe()
                                }
                )

    }


}