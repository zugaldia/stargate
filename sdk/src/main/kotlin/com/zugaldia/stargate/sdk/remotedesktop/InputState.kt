package com.zugaldia.stargate.sdk.remotedesktop

import org.freedesktop.dbus.types.UInt32

/**
 * Represents the state of an input device (button or key).
 * Used for NotifyPointerButton, NotifyKeyboardKeycode, and NotifyKeyboardKeysym.
 */
enum class InputState(val value: UInt32) {
    RELEASED(UInt32(0)),
    PRESSED(UInt32(1))
}
