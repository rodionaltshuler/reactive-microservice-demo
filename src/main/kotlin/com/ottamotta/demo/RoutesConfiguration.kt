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
    fun route(exchangeService: ExchangeService,
              streamService : StreamingService,
              repository: TickHistoryRepository,
              env: Environment) = router {
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

        GET("/streams/range/{market}") {
            System.out.println("Getting stream for ${it.pathVariable("market")}")
            ok().bodyToServerSentEvents(streamService.range(it.pathVariable("market")))
        }

        GET("/streams/{market}") {
            System.out.println("Getting stream for ${it.pathVariable("market")}")
            ok().bodyToServerSentEvents(streamService.listen(it.pathVariable("market")))
        }

        POST("/streams/{market}") {
            it.bodyToMono(String::class.java)
                    .flatMap { s -> ok().body(streamService.push(it.pathVariable("market"), s), String::class.java)}

        }
    }

}