package com.zugaldia.stargate.sdk.screencast

import org.freedesktop.dbus.types.UInt32

private const val MONITOR_BITMASK = 1
private const val WINDOW_BITMASK = 2
private const val VIRTUAL_BITMASK = 4

/**
 * Represents the available source types for screencasting.
 * These are defined as a bitmask in the portal specification.
 */
enum class SourceType(val value: Int) {
    MONITOR(MONITOR_BITMASK),
    WINDOW(WINDOW_BITMASK),
    VIRTUAL(VIRTUAL_BITMASK);

    companion object {
        /**
         * Parses a bitmask value into a set of source types.
         */
        fun fromBitmask(bitmask: UInt32): Set<SourceType> {
            val mask = bitmask.toInt()
            return entries.filter { (mask and it.value) != 0 }.toSet()
        }

        /**
         * Converts a set of source types into a bitmask value.
         */
        fun toBitmask(types: Set<SourceType>): UInt32 =
            UInt32(types.fold(0L) { acc, type -> acc or type.value.toLong() })
    }
}
