package com.ottamotta.demo

import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class TickHistoryRepository(val redis : ReactiveRedisOperations<String, TickerWithTS>) {

    val PREFIX = "history"

    fun findAll(market: String, from: Long, to: Long) : Flux<TickerWithTS> {
        //TODO get only within timerange specified
        return redis.opsForList().range(PREFIX + market, 0, -1)
    }

    fun save(market : String, ticker : TickerWithTS) : Mono<Long> =
         redis.opsForList().rightPush(PREFIX + market, ticker)

}