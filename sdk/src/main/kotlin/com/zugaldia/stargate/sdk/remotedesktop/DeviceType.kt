package com.zugaldia.stargate.sdk.remotedesktop

import org.freedesktop.dbus.types.UInt32

private const val KEYBOARD_BITMASK = 1
private const val POINTER_BITMASK = 2
private const val TOUCHSCREEN_BITMASK = 4

/**
 * Represents the available device types for remote desktop input.
 * These are defined as a bitmask in the portal specification.
 */
enum class DeviceType(val value: Int) {
    KEYBOARD(KEYBOARD_BITMASK),
    POINTER(POINTER_BITMASK),
    TOUCHSCREEN(TOUCHSCREEN_BITMASK);

    companion object {
        /**
         * Parses a bitmask value into a set of device types.
         */
        fun fromBitmask(bitmask: UInt32): Set<DeviceType> {
            val mask = bitmask.toInt()
            return entries.filter { (mask and it.value) != 0 }.toSet()
        }

        /**
         * Converts a set of device types into a bitmask value.
         */
        fun toBitmask(types: Set<DeviceType>): UInt32 =
            UInt32(types.fold(0L) { acc, type -> acc or type.value.toLong() })
    }
}
