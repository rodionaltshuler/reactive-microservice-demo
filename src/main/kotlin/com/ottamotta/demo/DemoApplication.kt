package com.ottamotta.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.router

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(DemoApplication::class.java)
            .initializers(beans {
                bean {
                    router {
                        GET("/hello") { ok().syncBody(SampleResponse()) }
                    }
                }
            })
            .run(*args)
}


