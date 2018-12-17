package com.ottamotta.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.bodyToServerSentEvents
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux
import java.time.Duration

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(DemoApplication::class.java)
            .initializers(beans {
                bean {
                    bean<WebClient> {
                        WebClient.builder()
                                .baseUrl(env.getProperty("exchange.url").orEmpty())
                                .build()
                    }
                    router {
                        val exchangeService = ref<ExchangeService>()
                        GET("/hello") { ok().syncBody(SampleResponse()) }
                        GET("/tick") {
                            val market = it.queryParam("market").orElse(env.getProperty("exchange.defaultMarket"))
                            ok().bodyToServerSentEvents(
                                    exchangeService.observe(market))
                        }
                    }
                }
            })
            .run(*args)
}

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Service
class ExchangeService(private val webClient: WebClient) {

    @Value("\${exchange.interval}")
    fun observe(market: String): Flux<TickEvent> {
        return Flux.interval(Duration.ofSeconds(5))
                .flatMap {
                    webClient
                            .get()
                            .uri {
                                it.path("/public/getticker")
                                        .queryParam("market", market)
                                        .build()
                            }
                            .retrieve()
                            .bodyToFlux(TickerResponse::class.java)
                }
                .map { TickEvent(tick = it.result, market = market, timestamp = System.currentTimeMillis()) }
    }


}

