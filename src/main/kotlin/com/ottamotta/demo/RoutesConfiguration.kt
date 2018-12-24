package com.ottamotta.demo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import org.springframework.web.reactive.function.server.router

@Configuration
class RoutesConfiguration {

    @Bean
    fun route(exchangeService: ExchangeService, env: Environment) =  router {
        GET("/hello") { ok().syncBody(SampleResponse()) }
        GET("/tick") {
            val market = it.queryParam("market").orElse(env.getProperty("exchange.defaultMarket"))
            ok().bodyToServerSentEvents(
                    exchangeService.observe(market))
        }
    }

}