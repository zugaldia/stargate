package com.zugaldia.stargate.sdk.request

import org.freedesktop.dbus.types.UInt32

/**
 * Indicates how the user interaction for a portal request ended.
 *
 * Portal methods that involve user interaction return a request handle and emit
 * a Response signal when the interaction completes. This enum represents the
 * possible outcomes of that interaction.
 */
enum class RequestResponse(val value: UInt32) {
    /** The request was carried out successfully. */
    SUCCESS(UInt32(0)),

    /** The user canceled the interaction. */
    CANCELLED(UInt32(1)),

    /** The user interaction ended in some other way. */
    OTHER(UInt32(2));

    companion object {
        /**
         * Returns the [RequestResponse] for the given value, or [OTHER] if unknown.
         */
        fun fromValue(value: UInt32): RequestResponse =
            entries.find { it.value == value } ?: OTHER
    }
}
