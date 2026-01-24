package com.zugaldia.stargate.app

import com.zugaldia.stargate.sdk.Printer

private const val LOOP_COUNT = 5

fun main() {
    val name = "Kotlin"
    val message = "Hello, $name!"
    val printer = Printer(message)
    printer.printMessage()

    for (i in 1..LOOP_COUNT) {
        println("i = $i")
    }
}
