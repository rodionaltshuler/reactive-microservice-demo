package com.ottamotta.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(DemoApplication::class.java)
            .run(*args)

}

