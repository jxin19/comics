package com.homework.comics

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ComicsApplication

fun main(args: Array<String>) {
    runApplication<ComicsApplication>(*args)
}
