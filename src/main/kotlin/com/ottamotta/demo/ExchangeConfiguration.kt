package com.ottamotta.demo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@Configuration
class ExchangeConfiguration {

    @Bean
    fun webClient(env : Environment) = WebClient.builder()
            .baseUrl(env.getProperty("exchange.url").orEmpty())
            .build();

    @Bean
    fun webClientWrapper(webClient: WebClient, redis : ReactiveRedisOperations<String, Ticker>, env: Environment) = WebClientWrapper(
            webClient, redis, Duration.ofSeconds(env.getRequiredProperty("exchange.interval").toLong() * 2))

    @Bean
    fun exchangeService(webClientWrapper: WebClientWrapper, env: Environment, repository: TickHistoryRepository) = ExchangeService(webClientWrapper, env, repository)


}