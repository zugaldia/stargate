package com.zugaldia.stargate.sdk.settings

import org.freedesktop.dbus.types.UInt32

/**
 * Color scheme preference from org.freedesktop.appearance.color-scheme.
 */
enum class ColorScheme(val value: UInt32) {
    NO_PREFERENCE(UInt32(0)),
    DARK(UInt32(1)),
    LIGHT(UInt32(2));

    companion object {
        fun fromValue(value: UInt32): ColorScheme =
            entries.find { it.value == value } ?: NO_PREFERENCE
    }
}

/**
 * Contrast preference from org.freedesktop.appearance.contrast.
 */
enum class Contrast(val value: UInt32) {
    NO_PREFERENCE(UInt32(0)),
    HIGHER(UInt32(1));

    companion object {
        fun fromValue(value: UInt32): Contrast =
            entries.find { it.value == value } ?: NO_PREFERENCE
    }
}

/**
 * Reduced motion preference from org.freedesktop.appearance.reduced-motion.
 */
enum class ReducedMotion(val value: UInt32) {
    NO_PREFERENCE(UInt32(0)),
    REDUCED(UInt32(1));

    companion object {
        fun fromValue(value: UInt32): ReducedMotion =
            entries.find { it.value == value } ?: NO_PREFERENCE
    }
}

/**
 * Accent color from org.freedesktop.appearance.accent-color.
 * RGB values in sRGB color space, range [0.0, 1.0].
 */
data class AccentColor(
    val red: Double,
    val green: Double,
    val blue: Double
)
