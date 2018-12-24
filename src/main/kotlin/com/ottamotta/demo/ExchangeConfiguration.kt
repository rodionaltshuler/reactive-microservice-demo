package com.ottamotta.demo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {

    @Bean
    fun webClient(env : Environment) = WebClient.builder()
            .baseUrl(env.getProperty("exchange.url").orEmpty())
            .build();

    @Bean
    fun webClientWrapper(webClient: WebClient) = WebClientWrapper(webClient)

    @Bean
    fun exchangeService(webClientWrapper: WebClientWrapper, env: Environment) = ExchangeService(webClientWrapper, env)


}