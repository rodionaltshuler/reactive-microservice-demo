package com.ottamotta.demo

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class CacheConfiguration {


    @Bean
    fun objectMapper() = ObjectMapper().apply {
        setVisibility(this.serializationConfig.defaultVisibilityChecker
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE))
    }

    @Bean
    fun operations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Ticker> {
        val serializer = Jackson2JsonRedisSerializer(Ticker::class.java)
        serializer.setObjectMapper(objectMapper())
        val builder = RedisSerializationContext.newSerializationContext<String, Ticker>(StringRedisSerializer())
        val context = builder.value(serializer).build()
        return ReactiveRedisTemplate<String, Ticker>(factory, context)
    }

    @Bean
    fun operationsWithTs(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, TickerWithTS> {
        val serializer = Jackson2JsonRedisSerializer(TickerWithTS::class.java)
        serializer.setObjectMapper(objectMapper())
        val builder = RedisSerializationContext.newSerializationContext<String, TickerWithTS>(StringRedisSerializer())
        val context = builder.value(serializer).build()
        return ReactiveRedisTemplate<String, TickerWithTS>(factory, context)
    }


}