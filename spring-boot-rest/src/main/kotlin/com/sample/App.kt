package com.sample

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class App {
    companion object {
        @JvmStatic public fun main(args: Array<String>) {
            SpringApplication.run(App::class.java, *args)
        }
    }
}
