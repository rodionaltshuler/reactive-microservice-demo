package com.ottamotta.demo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import org.springframework.web.reactive.function.server.router
import java.util.*

@Configuration
class RoutesConfiguration {

    @Bean
    fun route(exchangeService: ExchangeService, repository: TickHistoryRepository, env: Environment) = router {
        GET("/hello") { ok().syncBody(SampleResponse()) }
        GET("/tick") {
            val market = it.queryParam("market").orElse(env.getProperty("exchange.defaultMarket"))
            ok().bodyToServerSentEvents(
                    exchangeService.observe(market))
        }
        GET("/history/{market}") {
            //TODO add to and from query params

            fun parseParam(param : String, default :  Long) : Long = it.queryParam(param)
                    .filter { o -> Objects.nonNull(o) }
                    .map { s -> s.toLong() }
                    .orElse(default)

            val from  = parseParam("from", 0)
            val to  = parseParam("to", -1)

            val market = it.pathVariable("market")
            ok().body(repository.findAll(market, from, to), TickerWithTS::class.java)
        }
    }

}