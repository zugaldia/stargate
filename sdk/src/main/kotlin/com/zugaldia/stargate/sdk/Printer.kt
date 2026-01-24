package com.zugaldia.stargate.sdk

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

private const val DELAY_MS = 1000L

@Serializable
class Printer(val message: String) {
    @OptIn(ExperimentalTime::class)
    fun printMessage() = runBlocking {
        val now: Instant = Clock.System.now()
        launch {
            delay(DELAY_MS)
            println(now.toString())
        }
        println(message)
    }
}
