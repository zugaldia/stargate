package com.zugaldia.stargate.sdk.screencast

import org.freedesktop.dbus.types.UInt32

private const val HIDDEN_BITMASK = 1
private const val EMBEDDED_BITMASK = 2
private const val METADATA_BITMASK = 4

/**
 * Represents how the cursor should be rendered in the screencast stream.
 * These are defined as a bitmask in the portal specification.
 */
enum class CursorMode(val value: Int) {
    /**
     * The cursor is not part of the stream.
     */
    HIDDEN(HIDDEN_BITMASK),

    /**
     * The cursor is embedded into the stream.
     */
    EMBEDDED(EMBEDDED_BITMASK),

    /**
     * The cursor is not part of the screencast stream, but sent as PipeWire stream metadata.
     */
    METADATA(METADATA_BITMASK);

    companion object {
        /**
         * Parses a bitmask value into a set of cursor modes.
         */
        fun fromBitmask(bitmask: UInt32): Set<CursorMode> {
            val mask = bitmask.toInt()
            return entries.filter { (mask and it.value) != 0 }.toSet()
        }

        /**
         * Converts a set of cursor modes into a bitmask value.
         */
        fun toBitmask(modes: Set<CursorMode>): UInt32 =
            UInt32(modes.fold(0L) { acc, mode -> acc or mode.value.toLong() })
    }
}
