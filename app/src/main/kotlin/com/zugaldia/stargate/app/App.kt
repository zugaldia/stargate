package com.zugaldia.stargate.app

import com.zugaldia.stargate.sdk.DesktopPortal

fun main() {
    DesktopPortal.connect().use { portal ->
        println("Name: Settings Portal")
        println("Version: ${portal.settings.version}")

        portal.settings.getColorScheme()
            .onSuccess { println("Color Scheme: $it") }
            .onFailure { println("Color Scheme: Failed to read (${it.message})") }

        portal.settings.getAccentColor()
            .onSuccess { println("Accent Color: RGB(${it.red}, ${it.green}, ${it.blue})") }
            .onFailure { println("Accent Color: Failed to read (${it.message})") }

        portal.settings.getContrast()
            .onSuccess { println("Contrast: $it") }
            .onFailure { println("Contrast: Failed to read (${it.message})") }

        portal.settings.getReducedMotion()
            .onSuccess { println("Reduced Motion: $it") }
            .onFailure { println("Reduced Motion: Failed to read (${it.message})") }
    }
}
