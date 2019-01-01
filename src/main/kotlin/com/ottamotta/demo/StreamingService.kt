package com.ottamotta.demo

import io.lettuce.core.*
import io.lettuce.core.api.StatefulRedisConnection
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class StreamingService(private val redis: RedisClient) {

    private val SAMPLE_KEY = "key"

    private val pushConnection: StatefulRedisConnection<String, String>

    init {
        pushConnection = redis.connect()
    }

    fun push(stream: String, value: String): Mono<String> {
        val map = mapOf(SAMPLE_KEY to value)
        val args = XAddArgs().apply { id("*") }
        return pushConnection.reactive().xadd(stream, args, map)
    }

    fun range(stream: String): Flux<String> {
        val range: Range<String> = Range.create("-", "+")
        return redis.connect().reactive().xrange(stream, range)
                .doOnNext { m -> System.out.println(m.id) }
                .map { message -> message.body[SAMPLE_KEY] }
    }

    fun listen(stream: String): Flux<String> {
        System.out.println("listening to stream $stream")
        return longPoll(redis.connect(), stream, "$")
                .map { message -> message.body[SAMPLE_KEY] ?: "" }
                .doOnNext { System.out.println(it) }
    }


    private fun longPoll(connection: StatefulRedisConnection<String, String>, stream: String, offsetId: String): Flux<StreamMessage<String, String>> {
        System.out.println("polling new data starting with offset $offsetId")
        var nextId = offsetId
        val args = XReadArgs.Builder.block(1000);
        return connection.reactive().xread(args, XReadArgs.StreamOffset.from(stream, nextId))
                .doOnNext { m -> nextId = m.id }
                .concatWith {
                    longPoll(connection, stream, nextId).subscribe { next -> it.onNext(next) }
                }
                .retry()
    }


}