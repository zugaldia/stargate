package com.zugaldia.stargate.sdk.remotedesktop

import org.freedesktop.dbus.types.UInt32

/**
 * Represents the axis direction for discrete scroll events.
 * Used for NotifyPointerAxisDiscrete.
 */
enum class AxisDirection(val value: UInt32) {
    VERTICAL(UInt32(0)),
    HORIZONTAL(UInt32(1))
}
